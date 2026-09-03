// --- 基础工具 ---
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
    return null;
}

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

function showDevModal(featureName) {
    showModalMsg(`🌸 【${featureName}】正在由程序员熬夜开发中\n敬请期待下个版本！`, true);
}

function bindPressable(element, handler) {
    element.addEventListener('click', handler);
    element.addEventListener('keydown', (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            event.preventDefault();
            handler(event);
        }
    });
}

function normalizeAiText(text) {
    return (text || '')
        .replace(/\*\*(.*?)\*\*/g, '$1')
        .replace(/^\s*[-_]{3,}\s*$/gm, '')
        .replace(/\n{3,}/g, '\n\n')
        .trim();
}

function setPersonaText(content, text) {
    content.innerText = normalizeAiText(text);
    content.scrollTop = 0;
}
function bindPageActions() {
    document.querySelectorAll('[data-href]').forEach((item) => {
        bindPressable(item, () => { window.location.href = item.dataset.href; });
    });

    document.querySelectorAll('[data-close]').forEach((item) => {
        bindPressable(item, () => closeModal(item.dataset.close));
    });

    document.querySelectorAll('[data-dev-feature]').forEach((item) => {
        bindPressable(item, () => showDevModal(item.dataset.devFeature));
    });

    document.querySelectorAll('[data-action="user-info"]').forEach((item) => {
        bindPressable(item, openUserInfoModal);
    });

    document.querySelectorAll('[data-action="cycle-manager"]').forEach((item) => {
        bindPressable(item, openCycleManagerModal);
    });

    document.querySelectorAll('[data-action="persona"]').forEach((item) => {
        bindPressable(item, openPersonaModal);
    });

    bindPressable(document.getElementById('btn-ai-wallet'), () => showDevModal('AI 充值中心'));
    bindPressable(document.getElementById('profile-entry'), openUserInfoModal);
    bindPressable(document.getElementById('about-link'), () => showModalMsg('月月友：懂你的健康守护者\n当前版本：v1.0.0', true));

    document.getElementById('btn-submit-user-info').addEventListener('click', submitUserInfo);
    document.getElementById('btn-confirm-cycle-edit').addEventListener('click', confirmCycleEdit);
    document.getElementById('cycle-list-container').addEventListener('scroll', (event) => handleLazyLoad(event.currentTarget));
}


async function openPersonaModal() {
    const modal = document.getElementById('persona-modal');
    const content = document.getElementById('persona-content');
    modal.classList.add('show');

    if (personaCache) {
        setPersonaText(content, personaCache);
        return;
    }
    if (personaLoading) return;

    personaLoading = true;
    setPersonaText(content, 'AI 正在结合你的档案、周期记录和症状生成画像...');

    try {
        const res = await fetch(`${API_AI}/persona`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: USER_ID })
        });
        const json = await res.json();
        if (json.code === '0000' && json.data && json.data.content) {
            personaCache = normalizeAiText(json.data.content);
            setPersonaText(content, personaCache);
        } else {
            setPersonaText(content, json.info || 'AI 人物画像暂时不可用，请稍后再试');
        }
    } catch (error) {
        setPersonaText(content, '网络请求失败，请检查后端服务或稍后再试');
    } finally {
        personaLoading = false;
    }
}
// --- 全局变量 ---
let USER_ID = parseInt(getCookie('userId') || '', 10);
function resolveApiBase() {
    if (typeof window.__RECORD_ME_API_BASE__ === 'string') {
        return window.__RECORD_ME_API_BASE__;
    }

    const protocol = window.location.protocol;
    const hostname = window.location.hostname;
    const port = window.location.port;
    const isLocalHost = hostname === 'localhost' || hostname === '127.0.0.1' || hostname === '';
    const isLocalStaticServer = isLocalHost && port && port !== '80' && port !== '8088';

    if (protocol === 'file:' || isLocalStaticServer) {
        return 'http://127.0.0.1:8088';
    }

    return '';
}

const BASE_URL = resolveApiBase();
const API_MINE = `${BASE_URL}/record/mine`;
const API_AI = `${BASE_URL}/record/ai`;
let currentUserData = { username: "加载中...", avatarSeed: "user" };
let personaCache = null;
let personaLoading = false;
const IS_AUTHENTICATED = !Number.isNaN(USER_ID);

if (!IS_AUTHENTICATED) {
    window.location.replace('login.html');
}

const fmtDate = (dateStr) => dateStr ? dateStr.split('T')[0] : "";

document.addEventListener("DOMContentLoaded", () => {
    if (!IS_AUTHENTICATED) return;

    bindPageActions();
    fetchUserInfo();
});

// ================= 模块一：用户信息管理 =================
async function fetchUserInfo() {
    try {
        const res = await fetch(`${API_MINE}/getUsrInfo`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: USER_ID })
        });
        const json = await res.json();
        if (json.code === "0000" && json.data) {
            currentUserData = json.data;
            document.getElementById('user-name').innerText = currentUserData.username;

            if(currentUserData.avatar && currentUserData.avatar.trim() !== "") {
                document.getElementById('user-avatar').style.backgroundImage = `url('${currentUserData.avatar}')`;
                try {
                    const urlParams = new URLSearchParams(currentUserData.avatar.split('?')[1]);
                    currentUserData.avatarSeed = urlParams.get('seed') || currentUserData.username;
                } catch(e) { currentUserData.avatarSeed = currentUserData.username; }
            } else {
                const seed = currentUserData.username || "user";
                document.getElementById('user-avatar').style.backgroundImage = `url('https://api.dicebear.com/7.x/adventurer/svg?seed=${seed}')`;
                currentUserData.avatarSeed = seed;
            }
        }
    } catch (error) { console.error("拉取用户信息失败", error); }
}

function openUserInfoModal() {
    document.getElementById('edit-username').value = currentUserData.username || "";
    document.getElementById('edit-phone').value = currentUserData.phone || "";
    document.getElementById('edit-birthday').value = fmtDate(currentUserData.birthday) || "";
    document.getElementById('edit-height').value = currentUserData.height || "";
    document.getElementById('edit-weight').value = currentUserData.weight || "";
    updatePreviewAvatar(currentUserData.avatarSeed);
    document.getElementById('user-info-modal').classList.add('show');
}

document.getElementById('avatar-random-btn').addEventListener('click', () => {
    const randomSeed = Math.random().toString(36).substring(7);
    currentUserData.avatarSeed = randomSeed;
    updatePreviewAvatar(randomSeed);
});

function updatePreviewAvatar(seed) {
    document.getElementById('edit-avatar-preview').style.backgroundImage = `url('https://api.dicebear.com/7.x/adventurer/svg?seed=${seed}')`;
}

async function submitUserInfo() {
    const submitBtn = document.getElementById('btn-submit-user-info');
    submitBtn.classList.add('is-loading');
    submitBtn.disabled = true;

    const payload = {
        userId: USER_ID,
        username: document.getElementById('edit-username').value.trim(),
        avatar: `https://api.dicebear.com/7.x/adventurer/svg?seed=${currentUserData.avatarSeed}`,
        phone: document.getElementById('edit-phone').value.trim(),
        birthday: document.getElementById('edit-birthday').value || null,
        height: parseFloat(document.getElementById('edit-height').value) || null,
        weight: parseFloat(document.getElementById('edit-weight').value) || null
    };

    if (!payload.username) {
        submitBtn.classList.remove('is-loading');
        submitBtn.disabled = false;
        return showModalMsg("昵称不能为空哦！", true);
    }

    try {
        const res = await fetch(`${API_MINE}/changeUserInfo`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const json = await res.json();
        if (json.code === "0000") {
            closeModal('user-info-modal');
            fetchUserInfo();
            showModalMsg("档案保存成功 🌸", true);
        } else {
            showModalMsg(json.info || "保存失败", true);
        }
    } catch (error) {
        showModalMsg("网络请求异常", true);
    } finally {
        submitBtn.classList.remove('is-loading');
        submitBtn.disabled = false;
    }
}

// ================= 模块二：生理期管理 (分页懒加载：page/pageSize) =================
let cycleCurrentPage = 1;
let cyclePageSize = 2;
let totalRecordsCount = 0;
let isCycleLoading = false;
let hasMoreCycles = true;

function openCycleManagerModal() {
    cycleCurrentPage = 1;
    cyclePageSize = 2;
    totalRecordsCount = 0;
    hasMoreCycles = true;

    document.getElementById('cycle-cards-wrapper').innerHTML = '';
    document.getElementById('cycle-loading-text').innerText = "努力加载中...";
    document.getElementById('cycle-list-container').scrollTop = 0;
    document.getElementById('cycle-manage-modal').classList.add('show');

    loadCycleRecords();
}

async function loadCycleRecords() {
    if (isCycleLoading || !hasMoreCycles) return;

    isCycleLoading = true;
    const loadText = document.getElementById('cycle-loading-text');
    loadText.innerText = "努力加载中...";

    try {
        const res = await fetch(`${API_MINE}/getUserRecord`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userId: USER_ID, page: cycleCurrentPage, pageSize: cyclePageSize })
        });
        const json = await res.json();

        if (json.code === "0000" && json.data) {
            totalRecordsCount = json.data.recordsCount || 0;
            const recordsArray = json.data.cycleRecords || [];

            if (recordsArray.length > 0) {
                renderCycleCards(recordsArray);
                cycleCurrentPage++;
            }

            if (recordsArray.length < cyclePageSize) {
                hasMoreCycles = false;
                loadText.innerText = totalRecordsCount === 0 ? "暂无数据记录" : "已经到底啦~";
            } else {
                loadText.innerText = "向下滑动加载更多...";
            }
        } else {
            hasMoreCycles = false;
            loadText.innerText = "暂无数据记录";
        }
    } catch (error) {
        loadText.innerText = "加载失败，请检查网络";
    } finally {
        isCycleLoading = false;
    }
}

function renderCycleCards(records) {
    const container = document.getElementById('cycle-cards-wrapper');
    records.forEach(rec => {
        const start = fmtDate(rec.startDate) || "未知";
        const end = fmtDate(rec.endDate) || "至今未走";

        const card = document.createElement('div');
        card.className = 'cycle-card';
        card.innerHTML = `
            <div class="cycle-info">
                <div class="cycle-id-tag">周期 #${rec.cycleId}</div>
                <div class="cycle-dates">起：<span>${start}</span></div>
                <div class="cycle-dates">止：<span>${end}</span></div>
            </div>
            <button class="cycle-edit-btn">修改</button>
        `;
        card.querySelector('.cycle-edit-btn').addEventListener('click', () => openCycleEdit(rec.cycleId, start, end));
        container.appendChild(card);
    });
}

function handleLazyLoad(element) {
    // 当滚动到底部不足 20px 时触发下一批数据加载
    if (element.scrollTop + element.clientHeight >= element.scrollHeight - 20) {
        loadCycleRecords();
    }
}

// ================= 模块三：单次周期编辑 =================
function openCycleEdit(cycleId, start, end) {
    document.getElementById('edit-cycle-id').value = cycleId;
    document.getElementById('edit-cycle-start').value = start !== "未知" ? start : "";
    document.getElementById('edit-cycle-end').value = end !== "至今未走" ? end : "";
    document.getElementById('cycle-edit-modal').classList.add('show');
}

async function confirmCycleEdit() {
    const confirmBtn = document.getElementById('btn-confirm-cycle-edit');
    const cycleId = document.getElementById('edit-cycle-id').value;
    const start = document.getElementById('edit-cycle-start').value;
    const end = document.getElementById('edit-cycle-end').value || null;

    if (!start) return showModalMsg("必须填写开始时间哦！", true);

    const isConfirm = await showModalMsg(`⚠️ 警告\n确定要修改周期 #${cycleId} 的数据吗？\n错误的修改会影响 AI 预测准确度哦！`);
        if (!isConfirm) return;

    confirmBtn.classList.add('is-loading');
    confirmBtn.disabled = true;

    try {
        const res = await fetch(`${API_MINE}/changeUserRecord`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ cycleId: parseInt(cycleId), startDate: start, endDate: end })
        });
        const json = await res.json();

        if (json.code === "0000" && json.data === true) {
            closeModal('cycle-edit-modal');
            showModalMsg("修改成功 🌸", true);
            openCycleManagerModal(); // 刷新整个列表
        } else {
            showModalMsg(json.info || "修改失败，请重试", true);
        }
    } catch (error) {
        showModalMsg("网络请求异常", true);
    } finally {
        confirmBtn.classList.remove('is-loading');
        confirmBtn.disabled = false;
    }
}
