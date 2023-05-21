import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import jwt_decode from 'jwt-decode';
import axios from 'axios';
import '../styles/Login.css';


const LoginPage = () => {
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");
    const [idToken, setIdToken] = useState("");
    const [item, setItem] = useState([]);
    const navigate = useNavigate();
    const [decodedToken, setDecodedToken] = useState('');


    useEffect(() => {
        console.log("useEffect 실행");
        const urlParams = new URLSearchParams(window.location.search);
        const token = urlParams.get("token");
        if (token) { //token이 null이 아니면
            setIdToken(token);
            console.log("토큰: ", token);
        }
    }, []);

    // idToken 상태 변경에 대한 useEffect 추가
    useEffect(() => {
        if (idToken) {
            const decoded = jwt_decode(idToken);
            setDecodedToken(decoded);
            console.log("인증 토큰:", idToken);
        }
    }, [idToken]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const loginData = {
                id: id,
                password: password
            };

            const response = await axios.post('/api/user/login', loginData, {
                headers: { "Content-Type": "application/json" },
                withCredentials: true,
            });

            console.log("받은 토큰 : ", response.data);
            localStorage.setItem('jwt', response.data);
            console.log("로컬 스토리지에 저장된 토큰 : ", localStorage.getItem('jwt'))
            alert('로그인 되었습니다.');
            // navigate('/');

        } catch (error) {
            console.log(error);
            alert('ID 혹은 비밀번호가 일치하지 않습니다.');
        }
    };

    const showList = async () => {
        try {
            let token = localStorage.getItem('jwt');
            if (!token) {
                const response = await axios.get('/api/getItems', {
                    headers: { 'Authorization': `Bearer ${idToken}` },
                    withCredentials: true
                })
                console.log(response.data);
                setItem(response.data)
            } else {
                const response = await axios.get('/api/getItems', {
                    // headers: { 'AUTH-TOKEN': `${token}` },
                    headers: { 'Authorization': `Bearer ${token}` },
                    withCredentials: true
                });
                console.log(response.data);
                setItem(response.data)
            }
        } catch (error) {
            console.error("Items 가져오는데 실패 : ", error);
        }
    }

    const addBoard = async () => {
        try {
            let token = localStorage.getItem('jwt');
    
            const response = await fetch('/api/board/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // 'AUTH-TOKEN': `${token}`,
                    'Authorization': `Bearer ${token}`
                },
                credentials: 'include',
                body: JSON.stringify({id:id, password:password, user : {id:id}})
            });
    
            if (!response.ok) {
                throw new Error('게시글 등록 실패');
            }
    
            const responseData = await response.json();
            console.log(responseData);
            setItem(responseData);
        
        } catch (error) {
            console.error("게시글 등록 실패 : ", error);
        }
    }
    

    return (
        <div className="logindiv1">
            <form className="loginform1" onSubmit={handleSubmit}>
                <div>
                    < input type="text" id="id" value={id} placeholder="아이디"
                        onChange={(e) => setId(e.target.value)} required />
                </div>
                <div>
                    < input type="password" id="password" value={password} placeholder="비밀번호"
                        onChange={(e) => setPassword(e.target.value)} required />
                </div>
                <button type="submit">로그인</button>
            </form>
            <Link to="/register">회원가입</Link>
            <div className='logbutton'>
                <button onClick={() => { window.location.href = 'http://localhost:8080/oauth2/authorization/google'; }}>Login with Google</button>
            </div>
            <button onClick={showList}>리스트 보기</button>
            <table border={1}>
                <thead>
                    <tr>
                        <th>품목명</th>
                        <th>발주처</th>
                        <th>리드타임</th>
                        <th>어셈블리</th>
                        <th>기계</th>
                        <th>부품번호</th>
                    </tr>
                </thead>
                <tbody>
                    {item.map((item, index) => (
                        <tr key={index}>
                            <td>{item.item}</td>
                            <td>{item.company}</td>
                            <td>{item.leadtime}</td>
                            <td>{item.assembly}</td>
                            <td>{item.machinery}</td>
                            <td>{item.partNo1}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button onClick={addBoard}>게시글 추가</button>
        </div>

    );
}
export default LoginPage;
