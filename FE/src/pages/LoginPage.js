import {BrowserRouter, Route, Routes} from 'react-router-dom';
import { useState } from 'react';
import { Link, useNavigate  } from 'react-router-dom';
import axios from 'axios';

const LoginPage = () => {
  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('/Login', {
        id,
        password,
      });
      console.log(response.data); // 서버에서 반환한 데이터 출력
      alert('로그인되었습니다.');
      // 로그인 완료 후 메인 페이지로 이동 등의 처리
      navigate('/');
    } catch (error) {
      console.error(error);
      alert('ID혹은 비밀번호가 일치하지 않습니다.');
    }
  };

  return (
    <>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="id">ID:</label>
          <input
            type="text"
            id="id"
            value={id}
            onChange={(e) => setId(e.target.value)}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit">Login</button>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register</Link>
      </p>
    </>
  );
};

export default LoginPage;
