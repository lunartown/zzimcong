import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signUp, sendVerificationEmail, verifyEmail } from "../services/auth";

const Signup = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [phone, setPhone] = useState("");
  const [verificationCode, setVerificationCode] = useState("");
  const [error, setError] = useState("");
  const [isEmailSent, setIsEmailSent] = useState(false);
  const [isEmailVerified, setIsEmailVerified] = useState(false);
  const [verificationToken, setVerificationToken] = useState(""); // 인증 토큰 상태 추가
  const navigate = useNavigate();

  const handleSendVerificationEmail = async () => {
    try {
      await sendVerificationEmail(email);
      setIsEmailSent(true);
      setError("");
    } catch (err) {
      setError("인증 이메일 전송에 실패했습니다. 다시 시도해주세요.");
    }
  };

  const handleVerifyEmail = async () => {
    try {
      const response = await verifyEmail(email, verificationCode);
      setIsEmailVerified(true);
      setVerificationToken(response.token); // 이메일 인증 후 반환된 토큰 저장
      setError("");
    } catch (err) {
      setError(
        "이메일 인증에 실패했습니다. 코드를 확인하고 다시 시도해주세요."
      );
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!isEmailVerified) {
      setError("회원가입 전에 이메일을 인증해주세요.");
      return;
    }
    try {
      await signUp({ email, password, name, phone, token: verificationToken }); // 토큰을 회원가입 요청에 포함
      navigate("/login");
    } catch (err) {
      setError("회원가입에 실패했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10">
      <h2 className="text-2xl font-bold mb-5">회원가입</h2>
      {error && <p className="text-red-500 mb-5">{error}</p>}
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="email" className="block mb-1">
            이메일
          </label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="w-full px-3 py-2 border rounded"
            disabled={isEmailVerified}
          />
          {!isEmailVerified && (
            <button
              type="button"
              onClick={handleSendVerificationEmail}
              className="mt-2 bg-green-500 text-white py-1 px-3 rounded hover:bg-green-600"
              disabled={isEmailSent}
            >
              {isEmailSent ? "이메일 전송됨" : "인증 이메일 보내기"}
            </button>
          )}
        </div>
        {isEmailSent && !isEmailVerified && (
          <div>
            <label htmlFor="verificationCode" className="block mb-1">
              인증 코드
            </label>
            <input
              type="text"
              id="verificationCode"
              value={verificationCode}
              onChange={(e) => setVerificationCode(e.target.value)}
              required
              className="w-full px-3 py-2 border rounded"
            />
            <button
              type="button"
              onClick={handleVerifyEmail}
              className="mt-2 bg-blue-500 text-white py-1 px-3 rounded hover:bg-blue-600"
            >
              인증하기
            </button>
          </div>
        )}
        {isEmailVerified && (
          <>
            <div>
              <label htmlFor="password" className="block mb-1">
                비밀번호
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div>
              <label htmlFor="name" className="block mb-1">
                이름
              </label>
              <input
                type="text"
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <div>
              <label htmlFor="phone" className="block mb-1">
                전화번호
              </label>
              <input
                type="tel"
                id="phone"
                value={phone}
                onChange={(e) => setPhone(e.target.value)}
                required
                className="w-full px-3 py-2 border rounded"
              />
            </div>
            <button
              type="submit"
              className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
            >
              회원가입
            </button>
          </>
        )}
      </form>
    </div>
  );
};

export default Signup;
