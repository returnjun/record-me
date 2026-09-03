package top.daoha.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import top.daoha.api.IAiService;
import top.daoha.api.dto.AiAdviceResponseDTO;
import top.daoha.api.dto.UserIdRequestDTO;
import top.daoha.api.response.Response;
import top.daoha.domain.cycle.model.aggregate.CycleHistoryAggregate;
import top.daoha.domain.cycle.model.aggregate.IndexInfoAggregate;
import top.daoha.domain.cycle.model.entity.CycleRecordEntity;
import top.daoha.domain.cycle.service.ICycleHistoryService;
import top.daoha.domain.cycle.service.ICycleService;
import top.daoha.domain.health.model.entity.DailySymptomEntity;
import top.daoha.domain.health.service.IHealthTrackService;
import top.daoha.domain.identity.service.IAuthService;
import top.daoha.trigger.llm.DeepSeekClient;
import top.daoha.types.enums.ResponseCode;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/record/ai/")
public class AiController implements IAiService {

    @Resource
    private IAuthService authService;

    @Resource
    private ICycleService cycleService;

    @Resource
    private ICycleHistoryService cycleHistoryService;

    @Resource
    private IHealthTrackService healthTrackService;

    @Resource
    private DeepSeekClient deepSeekClient;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @RequestMapping(value = "health_advice", method = RequestMethod.POST)
    @Override
    public Response<AiAdviceResponseDTO> getHealthAdvice(@RequestBody UserIdRequestDTO userIdRequestDTO) {
        return generate(userIdRequestDTO, false);
    }

    @RequestMapping(value = "persona", method = RequestMethod.POST)
    @Override
    public Response<AiAdviceResponseDTO> getPersona(@RequestBody UserIdRequestDTO userIdRequestDTO) {
        return generate(userIdRequestDTO, true);
    }

    private Response<AiAdviceResponseDTO> generate(UserIdRequestDTO request, boolean personaMode) {
        try {
            if (request == null || request.getUserId() == null) {
                return Response.<AiAdviceResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            String context = buildUserHealthContext(request.getUserId(), personaMode);
            String content = deepSeekClient.chat(systemPrompt(personaMode), userPrompt(context, personaMode));

            return Response.<AiAdviceResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(AiAdviceResponseDTO.builder().content(content).build())
                    .build();
        } catch (Exception e) {
            log.error("AI 分析失败 userId={}, personaMode={}", request == null ? null : request.getUserId(), personaMode, e);
            return Response.<AiAdviceResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("AI 分析暂时不可用，请稍后再试")
                    .build();
        }
    }

    private String buildUserHealthContext(Long userId, boolean personaMode) {
        StringBuilder context = new StringBuilder();
        top.daoha.domain.identity.model.entity.UserEntity user = authService.queryUserInfo(userId);
        if (user != null) {
            context.append("用户基础信息：\n");
            appendLine(context, "用户名", user.getUsername());
            appendLine(context, "生日", formatDate(user.getBirthday()));
            appendLine(context, "身高(cm)", user.getHeight());
            appendLine(context, "体重(kg)", user.getWeight());
        }

        IndexInfoAggregate indexInfo = loadIndexInfo(userId);
        if (indexInfo != null) {
            context.append("\n当前周期状态：\n");
            appendLine(context, "状态", indexInfo.getStatus() == null ? null : indexInfo.getStatus().getInfo());
            appendLine(context, "本次/最近开始时间", formatDate(indexInfo.getNowStatus() == null ? null : indexInfo.getNowStatus().getStartDate()));
            appendLine(context, "本次/最近结束时间", formatDate(indexInfo.getNowStatus() == null ? null : indexInfo.getNowStatus().getEndDate()));
            appendLine(context, "预测下次开始或本次开始", formatDate(indexInfo.getPredictedStartTime()));
            appendLine(context, "预测结束或上次结束", formatDate(indexInfo.getPredictedEndTime()));
            if (indexInfo.getUser() != null) {
                appendLine(context, "平均周期天数", indexInfo.getUser().getAvgCycleDays());
                appendLine(context, "平均经期天数", indexInfo.getUser().getAvgPeriodDays());
            }
        }

        CycleHistoryAggregate history = cycleHistoryService.getCycleRecordList(userId, personaMode ? 8 : 6);
        if (history != null && history.getCycleList() != null) {
            context.append("\n最近周期记录：\n");
            for (CycleRecordEntity record : history.getCycleList()) {
                context.append("- 周期#").append(record.getCycleId())
                        .append("，开始：").append(formatDate(record.getStartDate()))
                        .append("，结束：").append(formatDate(record.getEndDate()))
                        .append("，是否当前周期：").append(Integer.valueOf(1).equals(record.getIsActive()) ? "是" : "否")
                        .append("\n");
                appendSymptoms(context, record.getCycleId(), personaMode ? 5 : 3);
            }
        }

        return context.toString();
    }

    private IndexInfoAggregate loadIndexInfo(Long userId) {
        try {
            IndexInfoAggregate aggregate = cycleService.getIndexInfoAggregate(userId);
            if (aggregate != null && aggregate.getNowStatus() != null && aggregate.getUser() != null) {
                aggregate.addPredictedTime();
            }
            return aggregate;
        } catch (Exception e) {
            log.warn("AI 上下文读取首页聚合信息失败 userId={}", userId, e);
            return null;
        }
    }

    private void appendSymptoms(StringBuilder context, Long cycleId, int limit) {
        if (cycleId == null) {
            return;
        }
        List<DailySymptomEntity> symptoms = healthTrackService.listByCycleId(cycleId);
        if (symptoms == null || symptoms.isEmpty()) {
            return;
        }
        int count = 0;
        for (DailySymptomEntity symptom : symptoms) {
            if (count >= limit) {
                break;
            }
            context.append("  症状：日期=").append(formatDate(symptom.getRecordDate()))
                    .append("，流量=").append(flowLabel(symptom.getFlowLevel()))
                    .append("，疼痛=").append(painLabel(symptom.getPainLevel()))
                    .append("，心情=").append(defaultText(symptom.getMood()))
                    .append("，备注=").append(defaultText(symptom.getNotes()))
                    .append("\n");
            count++;
        }
    }

    private String systemPrompt(boolean personaMode) {
        if (personaMode) {
            return "你是一个温和、谨慎的女性健康记录分析助手。请基于用户提供的周期、症状和身体基础信息生成用户画像。不要做疾病诊断，不要开药，不要制造焦虑；如发现明显风险，只建议线下咨询医生。";
        }
        return "你是一个温和、谨慎的女性健康建议助手。请基于用户当前生理期状态、周期历史和症状给出简短、可执行的健康建议。不要做疾病诊断，不要开药，不要制造焦虑；如发现明显异常，只建议线下咨询医生。";
    }

    private String userPrompt(String context, boolean personaMode) {
        if (personaMode) {
            return "请根据以下数据生成较全面的用户画像。输出要求：不要使用 Markdown 加粗符号、横线分隔符或代码块；使用普通文本小标题，依次包含：1. 周期规律性 2. 经期体验 3. 生活状态倾向 4. 需要关注的信号 5. 个性化护理建议。每部分 2-4 句，中文输出，语气亲切。\n\n" + context;
        }
        return "请根据以下数据给出首页 AI 健康建议。要求：不要使用 Markdown；总长度 120-220 字，先评价当前状态，再给 3 条具体建议，中文输出，语气亲切自然。\n\n" + context;
    }

    private void appendLine(StringBuilder builder, String label, Object value) {
        if (value == null || StringUtils.isBlank(String.valueOf(value))) {
            return;
        }
        builder.append("- ").append(label).append("：").append(value).append("\n");
    }

    private String formatDate(Date date) {
        return date == null ? "暂无" : dateFormat.format(date);
    }

    private String defaultText(String text) {
        return StringUtils.defaultIfBlank(text, "暂无");
    }

    private String flowLabel(Integer level) {
        if (level == null) {
            return "暂无";
        }
        switch (level) {
            case 0:
                return "少量";
            case 1:
                return "正常";
            case 2:
                return "多量";
            default:
                return String.valueOf(level);
        }
    }

    private String painLabel(Integer level) {
        if (level == null) {
            return "暂无";
        }
        switch (level) {
            case 0:
                return "轻微";
            case 1:
                return "正常";
            case 2:
                return "剧烈";
            default:
                return String.valueOf(level);
        }
    }
}
