import React from "react";
import "./auth.css";

const OAuthButtons = () => {
  const handleOAuthLogin = (provider) => {
    window.location.href = `http://localhost:8080/oauth2/authorize/${provider}`;
  };

  return (
    <div className="oauth-buttons">
      <button className="google-btn" onClick={() => handleOAuthLogin("google")}>
        Sign in with Google
      </button>
      <button className="github-btn" onClick={() => handleOAuthLogin("github")}>
        Sign in with GitHub
      </button>
    </div>
  );
};

export default OAuthButtons;
