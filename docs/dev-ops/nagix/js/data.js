// 获取 Cookies 工具函数
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
    return null;
}

let USER_ID = getCookie('userId') ? parseInt(getCookie('userId'), 10) : 5;
let USER_NAME = getCookie('username') || "元气少女";

// 使用浏览器当天作为日历和未结束周期的高亮基准
const todayDate = new Date();
todayDate.setHours(0, 0, 0, 0);
const TODAY_STR = formatDate(todayDate);

// 当前日历正在查看的年、月
let currentCalYear = todayDate.getFullYear();
let currentCalMonth = todayDate.getMonth(); // 0-11 代表 1-12月

// 全局数据缓存，用于日历和图表联动渲染
let globalRecords = [];

function bindPressable(element, handler) {
    element.addEventListener('click', handler);
    element.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            handler(event);
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById('user-name').innerText = USER_NAME;
    document.getElementById('countSelector').addEventListener('change', loadData);
    document.getElementById('btn-prev-month').addEventListener('click', () => changeMonth(-1));
    document.getElementById('btn-next-month').addEventListener('click', () => changeMonth(1));

    document.querySelectorAll('[data-href]').forEach((item) => {
        bindPressable(item, () => { window.location.href = item.dataset.href; });
    });

    // 绑定滚轮切换月份事件
    const calPanel = document.getElementById('calendarPanel');
    calPanel.addEventListener('wheel', (e) => {
        e.preventDefault();
        if (e.deltaY > 0) changeMonth(1);
        else changeMonth(-1);
    }, { passive: false });

    loadData();
});

// 核心异步数据请求加载
async function loadData() {
    const countVal = document.getElementById('countSelector').value;
    const payload = { userId: USER_ID, count: parseInt(countVal, 10) };

    try {
        const response = await fetch('http://127.0.0.1:8088/record/data/query_cycle_list', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const res = await response.json();
        if (res.code === "0000" && res.data) {
            renderStatistics(res.data);
        } else {
            renderFallbackData();
        }
    } catch (error) {
        console.error("网络请求异常，加载模拟Mock数据演示", error);
        renderFallbackData();
    }
}

// 渲染统计指标与触发联动重绘
function renderStatistics(data) {
    if (data.userName) document.getElementById('user-name').innerText = data.userName;
    if (data.avatar) document.getElementById('user-avatar').style.backgroundImage = `url('${data.avatar}')`;

    document.getElementById('avg-cycle').innerHTML = `${data.avgCycleDays || '--'}<span class="stat-unit">天</span>`;
    document.getElementById('avg-period').innerHTML = `${data.avgPeriodDays || '--'}<span class="stat-unit">天</span>`;

    globalRecords = data.cycleRecords || [];

    renderCalendar();
    renderTrendChart();
}

// 针对接口异常或无网环境的漂亮Mock兜底函数
function renderFallbackData() {
    const mockData = {
        "userId": USER_ID,
        "userName": USER_NAME,
        "avatar": "https://api.dicebear.com/7.x/adventurer/svg?seed=" + USER_NAME,
        "avgCycleDays": 19,
        "avgPeriodDays": 4,
        "cycleRecords": [
            { "cycleId": 7, "startDate": "2026-06-03T00:00:00.000+00:00", "endDate": null },
            { "cycleId": 2, "startDate": "2026-05-25T00:00:00.000+00:00", "endDate": "2026-06-02T00:00:00.000+00:00" },
            { "cycleId": 1, "startDate": "2026-04-25T00:00:00.000+00:00", "endDate": "2026-04-29T00:00:00.000+00:00" }
        ]
    };
    renderStatistics(mockData);
}

// 月份手动/滑轮切换
function changeMonth(direction) {
    currentCalMonth += direction;
    if (currentCalMonth > 11) { currentCalMonth = 0; currentCalYear++; }
    else if (currentCalMonth < 0) { currentCalMonth = 11; currentCalYear--; }
    renderCalendar();
}

// 核心日历算法：绘制与高亮判断
function renderCalendar() {
    const grid = document.getElementById('calendarGrid');
    const title = document.getElementById('calendar-title');
    grid.innerHTML = '';

    // 设置顶部月份文本显示
    title.innerText = `${currentCalYear} 年 ${(currentCalMonth + 1).toString().padStart(2, '0')} 月`;

    // 渲染周一到周日的表头
    const weekDays = ['一', '二', '三', '四', '五', '六', '日'];
    weekDays.forEach(day => {
        const cell = document.createElement('div');
        cell.className = 'weekday';
        cell.innerText = day;
        grid.appendChild(cell);
    });

    // 获取当前月份第1天和最后一天具体参数
    const firstDay = new Date(currentCalYear, currentCalMonth, 1);
    const lastDay = new Date(currentCalYear, currentCalMonth + 1, 0);

    // 计算周几偏移量 (让周一作为第一列)
    let startOffset = firstDay.getDay() - 1;
    if (startOffset < 0) startOffset = 6; // 周日处理成偏移6

    // 补齐上个月的尾巴日子
    const prevMonthLastDay = new Date(currentCalYear, currentCalMonth, 0).getDate();
    for (let i = startOffset - 1; i >= 0; i--) {
        const dayNum = prevMonthLastDay - i;
        const cell = document.createElement('div');
        cell.className = 'day-cell day-other-month';
        cell.innerText = dayNum;
        grid.appendChild(cell);
    }

    // 渲染本月所有核心天数
    const totalDays = lastDay.getDate();
    for (let d = 1; d <= totalDays; d++) {
        const cell = document.createElement('div');
        cell.className = 'day-cell';
        cell.innerText = d;

        // 构造标准日期对比串 YYYY-MM-DD
        const curDateObj = new Date(currentCalYear, currentCalMonth, d);
        const dateStr = formatDate(curDateObj);

        // 1. 判断是否属于生理期经期区间
        if (checkIsInPeriod(curDateObj)) {
            cell.classList.add('day-period');
        }

        // 2. 独立高亮系统“今天”
        if (dateStr === TODAY_STR) {
            cell.classList.add('day-today');
        }

        grid.appendChild(cell);
    }
}

// 格式化函数：消除时区干扰
function formatDate(date) {
    const y = date.getFullYear();
    const m = (date.getMonth() + 1).toString().padStart(2, '0');
    const d = date.getDate().toString().padStart(2, '0');
    return `${y}-${m}-${d}`;
}

// 精确匹配区间算法
function checkIsInPeriod(targetDate) {
    // 去除具体时分秒干扰
    const targetTime = new Date(targetDate.getFullYear(), targetDate.getMonth(), targetDate.getDate()).getTime();

    for (let record of globalRecords) {
        if (!record.startDate) continue;

        const start = new Date(record.startDate.split('T')[0] + "T00:00:00").getTime();
        let end;

        if (record.endDate) {
            end = new Date(record.endDate.split('T')[0] + "T00:00:00").getTime();
        } else {
            // 如果后端返回 endDate 为 null，则视作周期至今未完，一路加深渲染高亮到今天
            end = new Date(TODAY_STR + "T00:00:00").getTime();
        }

        if (targetTime >= start && targetTime <= end) {
            return true;
        }
    }
    return false;
}

// ==================== 原生高级 Canvas 趋势图表绘制 ====================
function renderTrendChart() {
    const canvas = document.getElementById('trendChart');
    if (!canvas) return;

    // 动态适配高DPI屏幕防止模糊
    const ctx = canvas.getContext('2d');
    const rect = canvas.getBoundingClientRect();
    const dpr = window.devicePixelRatio || 1;
    canvas.width = rect.width * dpr;
    canvas.height = rect.height * dpr;
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

    const width = rect.width;
    const height = rect.height;
    ctx.clearRect(0, 0, width, height);

    // 如果没有数据，绘制提示文案
    if (globalRecords.length === 0) {
        ctx.fillStyle = "#888";
        ctx.font = "14px sans-serif";
        ctx.textAlign = "center";
        ctx.fillText("暂无周期趋势数据", width / 2, height / 2);
        return;
    }

    // 解析并倒序排列数据（让时间线由远及近从左往右展示）
    const records = [...globalRecords].reverse();

    // 提取计算数据点
    let periodData = []; // 行经期长度
    let intervalData = []; // 完整周期长度

    for (let i = 0; i < records.length; i++) {
        const start = new Date(records[i].startDate.split('T')[0]);

        // 计算行经期
        let pDays = 4; // 缺省默认天数
        if (records[i].endDate) {
            pDays = Math.round((new Date(records[i].endDate.split('T')[0]) - start) / (1000 * 60 * 60 * 24)) + 1;
        } else {
            pDays = Math.round((todayDate - start) / (1000 * 60 * 60 * 24)) + 1;
        }
        periodData.push(pDays);

        // 计算周期天数（当前周期的开始时间减去上一次周期的开始时间）
        if (i > 0) {
            const prevStart = new Date(records[i-1].startDate.split('T')[0]);
            const cDays = Math.round((start - prevStart) / (1000 * 60 * 60 * 24));
            intervalData.push(cDays);
        } else {
            intervalData.push(19); // 初始首期基准缺省参照数据
        }
    }

    // 图表UI内部边界规划
    const paddingLeft = 30;
    const paddingRight = 20;
    const paddingTop = 20;
    const paddingBottom = 25;
    const chartW = width - paddingLeft - paddingRight;
    const chartH = height - paddingTop - paddingBottom;

    // 计算最大值范围
    const maxVal = Math.max(...periodData, ...intervalData, 28) + 4;

    // 映射坐标轴转化函数
    const getX = (index) => paddingLeft + (index / (records.length - 1 || 1)) * chartW;
    const getY = (val) => paddingTop + chartH - (val / maxVal) * chartH;

    // 绘制轻量横向背景虚网格线
    ctx.strokeStyle = "rgba(131, 144, 193, 0.1)";
    ctx.lineWidth = 1;
    for (let l = 0; l <= 4; l++) {
        const v = Math.round((maxVal / 4) * l);
        const y = getY(v);
        ctx.beginPath();
        ctx.moveTo(paddingLeft, y);
        ctx.lineTo(width - paddingRight, y);
        ctx.stroke();

        // 绘制左侧刻度数字文本
        ctx.fillStyle = "#a9b5d6";
        ctx.font = "10px sans-serif";
        ctx.textAlign = "right";
        ctx.fillText(v, paddingLeft - 6, y + 4);
    }

    // 1. 绘制“周期天数”折线（紫色渐变）
    drawTrendLine(ctx, records, intervalData, getX, getY, "#9d7dfa", "rgba(157, 125, 250, 0.15)", chartH + paddingTop);

    // 2. 绘制“行经天数”折线（粉色渐变）
    drawTrendLine(ctx, records, periodData, getX, getY, "#ff7eb3", "rgba(255, 126, 179, 0.15)", chartH + paddingTop);

    // 3. 绘制底部X轴刻度（月份简称标签）
    records.forEach((rec, idx) => {
        const d = new Date(rec.startDate.split('T')[0]);
        const label = `${d.getMonth() + 1}/${d.getDate()}`;
        ctx.fillStyle = "#8390c1";
        ctx.font = "10px sans-serif";
        ctx.textAlign = "center";
        ctx.fillText(label, getX(idx), height - 8);
    });
}

// 辅助绘制单条平滑曲线与数据小圆点方法
function drawTrendLine(ctx, records, dataList, getX, getY, color, areaColor, bottomY) {
    ctx.beginPath();
    for (let i = 0; i < dataList.length; i++) {
        if (i === 0) ctx.moveTo(getX(i), getY(dataList[i]));
        else ctx.lineTo(getX(i), getY(dataList[i]));
    }
    ctx.strokeStyle = color;
    ctx.lineWidth = 3;
    ctx.lineJoin = "round";
    ctx.stroke();

    // 填充折线下方半透明渐变区域
    ctx.lineTo(getX(dataList.length - 1), bottomY);
    ctx.lineTo(getX(0), bottomY);
    ctx.fillStyle = areaColor;
    ctx.fill();

    // 绘制亮色质感数据原点
    for (let i = 0; i < dataList.length; i++) {
        ctx.beginPath();
        ctx.arc(getX(i), getY(dataList[i]), 4, 0, Math.PI * 2);
        ctx.fillStyle = "#ffffff";
        ctx.fill();
        ctx.strokeStyle = color;
        ctx.lineWidth = 2;
        ctx.stroke();
    }
}
