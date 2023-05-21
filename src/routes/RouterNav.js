import {useNavigate} from 'react-router-dom';
import '../styles/RouterNav.css';

const RouterNav = () => {
    const navigate = useNavigate();

    const goLogin = () => {
        // page1 경로로 이동
        navigate('/login')
    };

    const goRegister = () => {
        navigate('/register')
    };

   
    return(
        <div className="rnavdiv1">
            <span onClick={goLogin}>로그인</span>
            <span onClick={goRegister}>회원가입</span>
        </div>
    );
}
export default RouterNav;
