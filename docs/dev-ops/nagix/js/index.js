function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
    return null;
}

let USER_ID = getCookie('userId');
let USER_NAME = getCookie('username');
let CYCLE_ID = null;

if (!USER_ID) USER_ID = 5; else USER_ID = parseInt(USER_ID, 10);

const BASE_URL = 'http://127.0.0.1:8088';
const API_BASE = `${BASE_URL}/record/index`;

let currentSymptoms = { flowLevel: 1, painLevel: 1, mood: "开心", notes: "无" };

const flowMap = { 0: "少量", 1: "正常", 2: "多量" };
const painMap = { 0: "轻微", 1: "正常", 2: "剧烈" };

const flowRevMap = { "少量": 0, "正常": 1, "多量": 2 };
const painRevMap = { "轻微": 0, "正常": 1, "剧烈": 2 };

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
    if (USER_NAME) document.getElementById('user-name').innerText = USER_NAME;

    document.querySelectorAll('[data-href]').forEach((item) => {
        bindPressable(item, () => { window.location.href = item.dataset.href; });
    });

    document.querySelectorAll('[data-close]').forEach((item) => {
        bindPressable(item, () => closeModal(item.dataset.close));
    });

    document.querySelectorAll('[data-symptom]').forEach((item) => {
        bindPressable(item, () => openSubModal(item.dataset.symptom));
    });

    document.getElementById('btn-submit-symptom').addEventListener('click', submitSymptomRecord);
    document.getElementById('btn-save-note').addEventListener('click', saveNote);

    // 为“发生了什么”按钮增加弹窗提示
    document.getElementById('btn-what').addEventListener('click', () => {
        showModalMsg("生活记录功能正在开发中哦~ 敬请期待！🌸", true);
    });
});

function closeModal(id) { document.getElementById(id).classList.remove('show'); }

function showModalMsg(message, isAlert = false) {
    return new Promise((resolve) => {
        const overlay = document.getElementById('custom-modal');
        const msgEl = document.getElementById('modal-msg');
        const btnCancel = document.getElementById('modal-cancel');
        const btnConfirm = document.getElementById('modal-confirm');

        msgEl.innerText = message;
        btnCancel.style.display = isAlert ? 'none' : 'block';
        btnConfirm.innerText = isAlert ? '我知道了' : '确认';
        overlay.classList.add('show');

        const cleanup = () => {
            overlay.classList.remove('show');
            btnConfirm.removeEventListener('click', onConfirm);
            btnCancel.removeEventListener('click', onCancel);
        };
        const onConfirm = () => { cleanup(); resolve(true); };
        const onCancel = () => { cleanup(); resolve(false); };

        btnConfirm.addEventListener('click', onConfirm);
        btnCancel.addEventListener('click', onCancel);
    });
}

async function fetchInitialData(userId) {
    try {
        const response = await fetch(`${API_BASE}/query_user_info`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: userId })
        });
        return await response.json();
    } catch (error) {
        return {
            "code": "0000", "data": {
                "userId": userId, "cycleId": 8, "userName": USER_NAME || "元气少女",
                "avatar": "https://api.dicebear.com/7.x/adventurer/svg?seed=" + (USER_NAME || "Felix"),
                "comeDays": "4天前", "goDays": "1天后", "status": 0,
                "aiSuggestion": "状态很好，很健康，保持好心情积极面对生活吧"
            }
        };
    }
}

function renderUI(data) {
    if (data.userName) document.getElementById('user-name').innerText = data.userName;
    if (data.avatar) document.getElementById('user-avatar').style.backgroundImage = `url('${data.avatar}')`;
    document.getElementById('ai-suggestion').innerText = data.aiSuggestion || "状态很好，很健康，保持好心情积极面对生活吧";
    if (data.comeDays) document.getElementById('date-come').innerText = data.comeDays;
    if (data.goDays) document.getElementById('date-go').innerText = data.goDays;
    const actionArea = document.getElementById('action-area');
    if (data.status === 0) actionArea.classList.remove('reverse');
    else if (data.status === 1) actionArea.classList.add('reverse');
}

async function init() {
    if (!USER_ID) return;
    const res = await fetchInitialData(USER_ID);
    if (res && res.code === "0000") {
        CYCLE_ID = res.data.cycleId;
        renderUI(res.data);
    }
}

async function handleAction(type) {
    let confirmMsg = type === 'come' ? "确定大姨妈来了吗？" : "确定大姨妈走了吗？";
    let endpoint = type === 'come' ? `${API_BASE}/start_cycle_record` : `${API_BASE}/over_cycle_record`;
    if (await showModalMsg(confirmMsg, false)) {
        const triggerBtn = document.getElementById(type === 'come' ? 'btn-come' : 'btn-go');
        triggerBtn.classList.add('is-loading');
        triggerBtn.disabled = true;
        try {
            const response = await fetch(endpoint, {
                method: 'POST', headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId: USER_ID })
            });
            const resData = await response.json();
            if (resData.code === "0000") init();
            else await showModalMsg(resData.info || "操作失败，请重试", true);
        } catch (error) {
            await showModalMsg("网络请求失败！", true);
        } finally {
            triggerBtn.classList.remove('is-loading');
            triggerBtn.disabled = false;
        }
    }
}

document.getElementById('btn-status').addEventListener('click', async () => {
    if (!CYCLE_ID) { await showModalMsg("暂无周期数据", true); return; }

    let payload = { cycleId: parseInt(CYCLE_ID), userId: parseInt(USER_ID), flowLevel: 1, painLevel: 1, mood: "", notes: "" };
    try {
        const response = await fetch(`${API_BASE}/query_symptom`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload)
        });
        const resData = await response.json();
        if (resData.code === "0000" && resData.data) updateLocalSymptoms(resData.data);
    } catch (error) {
        updateLocalSymptoms({ flowLevel: 1, painLevel: 1, mood: "正常", notes: "无" });
    }
    refreshSymptomUI();
    document.getElementById('symptom-record-modal').classList.add('show');
});

function updateLocalSymptoms(data) {
    let f = parseInt(data.flowLevel);
    let p = parseInt(data.painLevel);

    currentSymptoms.flowLevel = isNaN(f) ? 1 : f;
    currentSymptoms.painLevel = isNaN(p) ? 1 : p;
    currentSymptoms.mood = data.mood || "正常";
    currentSymptoms.notes = data.notes || "无";
}

function refreshSymptomUI() {
    document.getElementById('val-pain').innerText = painMap[currentSymptoms.painLevel] || "正常";
    document.getElementById('val-flow').innerText = flowMap[currentSymptoms.flowLevel] || "正常";
    document.getElementById('val-mood').innerText = currentSymptoms.mood;
    let noteStr = currentSymptoms.notes || "无";
    document.getElementById('val-note').innerText = noteStr;
}

function openSubModal(type) {
    if (type === 'note') {
        document.getElementById('note-textarea').value = currentSymptoms.notes === "无" ? "" : currentSymptoms.notes;
        document.getElementById('symptom-note-modal').classList.add('show');
        return;
    }

    const container = document.getElementById('options-container');
    container.innerHTML = '';

    let options = [];
    let currentValue = '';
    let currentType = type;

    if (type === 'pain') {
        options = ["轻微", "正常", "剧烈"];
        currentValue = painMap[currentSymptoms.painLevel];
    } else if (type === 'flow') {
        options = ["少量", "正常", "多量"];
        currentValue = flowMap[currentSymptoms.flowLevel];
    } else if (type === 'mood') {
        options = ["开心", "正常", "焦虑", "悲伤", "兴奋", "幸福"];
        currentValue = currentSymptoms.mood;
    }

    options.forEach(opt => {
        const btn = document.createElement('div');
        btn.className = 'option-btn' + (opt === currentValue ? ' selected' : '');
        btn.innerText = opt;
        btn.onclick = () => selectOption(currentType, opt);
        container.appendChild(btn);
    });

    document.getElementById('symptom-options-modal').classList.add('show');
}

function selectOption(type, value) {
    if (type === 'pain') {
        let mappedVal = painRevMap[value];
        currentSymptoms.painLevel = mappedVal !== undefined ? mappedVal : 1;
    }
    else if (type === 'flow') {
        let mappedVal = flowRevMap[value];
        currentSymptoms.flowLevel = mappedVal !== undefined ? mappedVal : 1;
    }
    else if (type === 'mood') currentSymptoms.mood = value;

    refreshSymptomUI();
    closeModal('symptom-options-modal');
}

function saveNote() {
    let val = document.getElementById('note-textarea').value.trim();
    currentSymptoms.notes = val === "" ? "无" : val;
    refreshSymptomUI();
    closeModal('symptom-note-modal');
}

async function submitSymptomRecord() {
    const submitBtn = document.getElementById('btn-submit-symptom');
    submitBtn.classList.add('is-loading');
    submitBtn.disabled = true;

    let payload = {
        cycleId: parseInt(CYCLE_ID) || 0,
        userId: parseInt(USER_ID) || 0,
        flowLevel: parseInt(currentSymptoms.flowLevel),
        painLevel: parseInt(currentSymptoms.painLevel),
        mood: currentSymptoms.mood,
        notes: currentSymptoms.notes === "无" ? "" : currentSymptoms.notes
    };

    try {
        const response = await fetch(`${API_BASE}/change_symptom`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const resData = await response.json();
        if (resData.code === "0000") {
            closeModal('symptom-record-modal');
            setTimeout(() => showModalMsg("状态记录已保存 🌸", true), 300);
        } else {
            await showModalMsg(resData.info || "非法参数或记录失败", true);
        }
    } catch (error) {
        console.log("网络请求异常", error);
        closeModal('symptom-record-modal');
    } finally {
        submitBtn.classList.remove('is-loading');
        submitBtn.disabled = false;
    }
}

document.getElementById('btn-come').addEventListener('click', () => handleAction('come'));
document.getElementById('btn-go').addEventListener('click', () => handleAction('go'));

window.addEventListener('load', init);

function initJourneyAnimation() {
    const canvas = document.getElementById('journeyCanvas');
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    function resize() {
        const rect = canvas.getBoundingClientRect();
        const dpr = window.devicePixelRatio || 1;
        canvas.width = rect.width * dpr;
        canvas.height = rect.height * dpr;
        ctx.setTransform(dpr, 0, 0, dpr, 0, 0);
    }
    resize(); window.addEventListener('resize', resize);
    let time = 0; const speed = 0.5; let friendState = 0; let friendX = -50; let stateTimer = 0;
    function draw() {
        const w = canvas.getBoundingClientRect().width; const h = canvas.getBoundingClientRect().height; ctx.clearRect(0, 0, w, h);
        time += 0.005; stateTimer += 1;
        const cy = h * 0.35; const radius = w * 0.45;
        const sunX = w/2 + Math.cos(time) * radius; const sunY = cy + Math.sin(time) * radius;
        const moonX = w/2 + Math.cos(time + Math.PI) * radius; const moonY = cy + Math.sin(time + Math.PI) * radius;
        if (sunY < h * 0.7) { ctx.beginPath(); ctx.arc(sunX, sunY, 14, 0, Math.PI * 2); ctx.fillStyle = '#ff9f43'; ctx.shadowBlur = 20; ctx.shadowColor = '#ff9f43'; ctx.fill(); ctx.shadowBlur = 0; }
        if (moonY < h * 0.7) { ctx.beginPath(); ctx.arc(moonX, moonY, 11, 0, Math.PI * 2); ctx.fillStyle = '#feca57'; ctx.shadowBlur = 15; ctx.shadowColor = '#feca57'; ctx.fill(); ctx.shadowBlur = 0; }
        const groundY = h * 0.75;
        ctx.fillStyle = 'rgba(157, 125, 250, 0.15)'; ctx.beginPath();
        for (let x = 0; x <= w; x += 10) { const y = groundY - 35 - Math.sin(x * 0.01 + time * 0.2) * 10 - Math.cos(x * 0.005) * 15; if (x === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y); }
        ctx.lineTo(w, h); ctx.lineTo(0, h); ctx.fill();
        ctx.fillStyle = 'rgba(107, 227, 233, 0.2)'; ctx.beginPath();
        for (let x = 0; x <= w; x += 10) { const y = groundY - 5 + Math.sin(x * 0.03 + time * 0.5) * 4; if (x === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y); }
        ctx.lineTo(w, h); ctx.lineTo(0, h); ctx.fill();
        ctx.strokeStyle = 'rgba(131, 144, 193, 0.3)'; ctx.lineWidth = 2; ctx.beginPath(); ctx.moveTo(0, groundY); ctx.lineTo(w, groundY); ctx.stroke();
        const playerX = w * 0.4; const bounce = Math.sin(stateTimer * 0.08) * 2;
        if (friendState === 0) { if (stateTimer > 400) { friendState = 1; friendX = w + 30; } }
        else if (friendState === 1) { friendX -= speed * 1.5; if (friendX <= playerX + 25) { friendState = 2; stateTimer = 0; } }
        else if (friendState === 2) { friendX = playerX + 22; if (stateTimer > 500) { friendState = 3; } }
        else if (friendState === 3) { friendX -= speed * 0.5; if (friendX < -30) { friendState = 0; stateTimer = 0; } }
        drawCharacter(ctx, playerX, groundY + bounce, '#ff7eb3');
        if (friendState > 0) {
            const friendBounce = Math.sin(stateTimer * 0.09) * 2; drawCharacter(ctx, friendX, groundY + friendBounce, '#9d7dfa');
            if (friendState === 2) { ctx.strokeStyle = '#ff7eb3'; ctx.lineWidth = 3; ctx.lineCap = 'round'; ctx.beginPath(); ctx.moveTo(playerX + 5, groundY + bounce - 12); ctx.lineTo(friendX - 5, groundY + friendBounce - 12); ctx.stroke(); }
        }
        requestAnimationFrame(draw);
    }
    function drawCharacter(context, x, y, color) {
        context.fillStyle = color; context.beginPath(); context.moveTo(x, y - 2); context.lineTo(x - 8, y - 18); context.lineTo(x + 8, y - 18); context.closePath(); context.fill();
        context.beginPath(); context.arc(x, y - 24, 6, 0, Math.PI * 2); context.fillStyle = '#555'; context.fill();
        context.strokeStyle = '#555'; context.lineWidth = 2; context.beginPath(); context.moveTo(x - 3, y - 2); context.lineTo(x - 3 + Math.sin(stateTimer * 0.1) * 3, y + 3); context.moveTo(x + 3, y - 2); context.lineTo(x + 3 - Math.sin(stateTimer * 0.1) * 3, y + 3); context.stroke();
    }
    draw();
}
initJourneyAnimation();
