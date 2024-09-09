import api from "./api";

export const login = async (credentials) => {
  const response = await api.post("/auth/login", credentials);
  localStorage.setItem("accessToken", response.data.accessToken);
  localStorage.setItem("refreshToken", response.data.refreshToken);
  return response.data.user;
};

export const logout = async () => {
  await api.post("/auth/logout");
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
};

export const refreshToken = async () => {
  const refreshToken = localStorage.getItem("refreshToken");
  const response = await api.post("/auth/refresh", { refreshToken });
  localStorage.setItem("accessToken", response.data.accessToken);
  return response.data.user;
};

export const signUp = async (email, password, name, phone, token) => {
  const response = await api.post(
    `/auth/signup?token=${token}`,
    email,
    password,
    name,
    phone
  );
  return response.data;
};

// 이메일 인증 코드 요청
export const sendVerificationEmail = async (email) => {
  const response = await api.post("/email-verifications/send", { email });
  return response.data;
};

// 이메일 인증 코드 확인
export const verifyEmail = async (email, authNum) => {
  const response = await api.post("/email-verifications/verify", {
    email,
    authNum,
  });
  return response.data;
};

// 이메일 중복 확인
export const checkEmailAvailability = async (email) => {
  const response = await api.post("/auth/check-email", { email });
  return response.data.isAvailable;
};

// 회원가입 프로세스 (이메일 인증 포함)
export const signUpWithEmailVerification = async (userData) => {
  // 1. 이메일 중복 확인
  const isEmailAvailable = await checkEmailAvailability(userData.email);
  if (!isEmailAvailable) {
    throw new Error("이미 사용 중인 이메일입니다.");
  }

  // 2. 이메일 인증 코드 요청
  await sendVerificationEmail(userData.email);

  // 3. 이메일 인증 코드 확인 (이 단계는 사용자의 입력을 기다려야 하므로 실제 구현에서는 별도의 단계로 처리해야 합니다)
  // await verifyEmail(userData.email, userData.verificationCode);

  // 4. 회원가입 완료
  const response = await signUp(userData);
  return response;
};
