import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import OAuthButtons from "./OAuthButtons";
import "./auth.css"; // Ensure you create this file for styling

const Auth = () => {
  const [isSignUp, setIsSignUp] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");
  const [roles, setRoles] = useState([]); // To store roles from backend
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    // Fetch roles from the backend API
    const fetchRoles = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/roles");
        const data = await response.json();
        setRoles(data);
      } catch (err) {
        console.error("Error fetching roles:", err);
      }
    };
    fetchRoles();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    const endpoint = isSignUp ? "/api/auth/signup" : "/api/auth/login";
    const payload = { email, password, ...(isSignUp && { role }) };

    try {
      const response = await fetch(`http://localhost:8080${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      const data = await response.json();
      if (!response.ok) throw new Error(data.message || "Authentication failed");

      // Handle successful authentication
      console.log("Success:", data);
      navigate("/dashboard"); // Redirect user after login/signup
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="auth-container">
      <h2>{isSignUp ? "Sign Up" : "Login"}</h2>

      {error && <p className="error">{error}</p>}

      <form onSubmit={handleSubmit}>
        <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />

        {isSignUp && (
          <select value={role} onChange={(e) => setRole(e.target.value)} required>
            <option value="">Select Role</option>
            {roles.map((r) => (
              <option key={r} value={r}>
                {r}
              </option>
            ))}
          </select>
        )}

        <button type="submit">{isSignUp ? "Sign Up" : "Login"}</button>
      </form>

      <OAuthButtons />

      <p onClick={() => setIsSignUp(!isSignUp)} className="toggle-link">
        {isSignUp ? "Already have an account? Login" : "Don't have an account? Sign up"}
      </p>
    </div>
  );
};

export default Auth;
