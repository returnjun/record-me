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
const API_AUTH = `${BASE_URL}/record/auth`;

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return decodeURIComponent(parts.pop().split(';').shift());
    return null;
}

if (getCookie('userId')) {
    window.location.replace('index.html');
}

let isLoginMode = true;
let isPasswordValid = true; // 默认登录模式不校验，设为true

const groupPhone = document.getElementById('group-phone');
const inpUsername = document.getElementById('inp-username');
const inpPhone = document.getElementById('inp-phone');
const inpPassword = document.getElementById('inp-password');
const btnSubmit = document.getElementById('btn-submit');
const btnToggle = document.getElementById('btn-toggle');

// 密码提示相关DOM
const pwdHintContainer = document.getElementById('pwd-hint');
const hintLen = document.getElementById('hint-len');
const hintLetter = document.getElementById('hint-letter');
const hintNum = document.getElementById('hint-num');
const hintSym = document.getElementById('hint-sym');

function setSubmitLoading(isLoading) {
    btnSubmit.disabled = isLoading;
    btnSubmit.classList.toggle('is-loading', isLoading);
    btnSubmit.innerText = isLoading ? (isLoginMode ? '登录中' : '注册中') : (isLoginMode ? 'gogogo' : '注 册');
}

// 基础弹窗方法
function showModal(message) {
    return new Promise((resolve) => {
        const overlay = document.getElementById('custom-modal');
        const msgEl = document.getElementById('modal-msg');
        const btnConfirm = document.getElementById('modal-confirm');

        msgEl.innerText = message;
        overlay.classList.add('show');

        const cleanup = () => {
            overlay.classList.remove('show');
            btnConfirm.removeEventListener('click', onConfirm);
        };

        const onConfirm = () => { cleanup(); resolve(true); };
        btnConfirm.addEventListener('click', onConfirm);
    });
}

// Cookie设置
function setCookie(name, value, days = 30) {
    const date = new Date();
    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    document.cookie = name + "=" + value + ";" + expires + ";path=/";
}

// 校验密码并更新UI
function validatePassword(pwd) {
    // 如果是登录模式，不进行校验限制
    if (isLoginMode) {
        isPasswordValid = true;
        return;
    }

    const hasLen = pwd.length >= 8;
    const hasLetter = /[a-zA-Z]/.test(pwd);
    const hasNum = /[0-9]/.test(pwd);
    const hasSym = /[^a-zA-Z0-9\s]/.test(pwd); // 匹配非字母数字和空白的字符

    // 更新实时清单UI
    hintLen.className = `hint-item ${hasLen ? 'valid' : ''}`;
    hintLetter.className = `hint-item ${hasLetter ? 'valid' : ''}`;
    hintNum.className = `hint-item ${hasNum ? 'valid' : ''}`;
    hintSym.className = `hint-item ${hasSym ? 'valid' : ''}`;

    // 综合判断
    isPasswordValid = hasLen && hasLetter && hasNum && hasSym;
}

// 监听密码输入事件
inpPassword.addEventListener('input', (e) => {
    if (!isLoginMode) {
        validatePassword(e.target.value);
    }
});

// 切换登录/注册模式
btnToggle.addEventListener('click', () => {
    isLoginMode = !isLoginMode;

    if (isLoginMode) {
        // 登录模式 UI
        groupPhone.classList.add('hidden');
        pwdHintContainer.classList.remove('show'); // 隐藏密码清单
        document.getElementById('label-account').innerText = '账号';
        inpUsername.placeholder = '用户名 / 手机号';
        btnSubmit.innerText = 'gogogo';
        btnToggle.innerText = '没有账号，点击注册';
        btnSubmit.style.background = 'linear-gradient(135deg, #ff7eb3 0%, #9d7dfa 100%)';
        btnSubmit.style.boxShadow = '0 8px 25px rgba(157, 125, 250, 0.4)';
        isPasswordValid = true; // 登录不校验复杂度
    } else {
        // 注册模式 UI
        groupPhone.classList.remove('hidden');
        pwdHintContainer.classList.add('show'); // 显示密码清单
        document.getElementById('label-account').innerText = '用户名';
        inpUsername.placeholder = '请输入用户名';
        validatePassword(inpPassword.value); // 立刻校验一次当前输入框内容
        btnSubmit.innerText = '注 册';
        btnToggle.innerText = '已有账号，点击登录';
        btnSubmit.style.background = 'linear-gradient(135deg, #17c3b2 0%, #43e97b 100%)';
        btnSubmit.style.boxShadow = '0 8px 25px rgba(23, 195, 178, 0.4)';
    }
});

// 提交按钮逻辑
btnSubmit.addEventListener('click', async () => {
    const username = inpUsername.value.trim();
    const password = inpPassword.value.trim();
    const phone = inpPhone.value.trim();

    if (!username) return showModal(isLoginMode ? "账号不能为空哦！" : "用户名不能为空哦！");
    if (!password) return showModal("密码不能为空哦！");

    // 注册模式下的特殊校验拦截
    if (!isLoginMode) {
        if (!phone) return showModal("注册需要填写手机号哦！");
        if (!isPasswordValid) return showModal("密码太简单啦，请参考输入框下方的提示修改一下哦！");
    }

    const payload = { username, password };
    if (!isLoginMode) {
        payload.phone = phone;
    }

    const endpoint = isLoginMode ? `${API_AUTH}/login` : `${API_AUTH}/register`;
    setSubmitLoading(true);

    setTimeout(async () => {
        try {
            const response = await fetch(endpoint, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const resData = await response.json();

            if (resData.code === "0000") {
                const userId = resData.data.userId;
                const returnedUsername = resData.data.username;

                setCookie('userId', userId);
                setCookie('username', returnedUsername);

                const successMsg = isLoginMode ? "欢迎回来，月月友！" : "注册成功，欢迎加入月月友！";

                await showModal(successMsg);
                window.location.href = "index.html";
            } else {
                await showModal(resData.info || "操作失败，请重试");
            }
        } catch (error) {
            await showModal("网络请求失败，请检查后端服务是否开启！");
            console.error("请求报错:", error);
        } finally {
            setSubmitLoading(false);
        }
    }, 150);
});
