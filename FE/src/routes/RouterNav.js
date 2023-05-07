import { useNavigate, Link } from 'react-router-dom';
import logoImage from '../images/ship.svg';
import cartImage from '../images/cartlist.svg';
import axios from 'axios';


const RouterNav = ({ isAuthenticated, setIsAuthenticated }) => {
    const navigate = useNavigate();

    const goLogin = () => {
        // page1 경로로 이동
        navigate('/login')
    };

    const goRegister = () => {
        navigate('/register')
    };

    const goCart = () => {
        navigate('/cart')
    };

    const handleLogout = async () => {
        try {
          // 서버에 로그아웃 요청을 보냅니다.
          await axios.post('/logout');
          setIsAuthenticated(false);
          localStorage.removeItem('isAuthenticated');
          alert('로그아웃 되었습니다.');
        } catch (error) {
          console.error(error);
          alert('로그아웃 처리 중 오류가 발생했습니다.');
        }
      };

    return (
        <div>
            <header style={{ background: 'lightgray', padding: 16, fontSize: 24 }}>
                <Link to="/">
                    <img src={logoImage} alt="ship" style={{ width: "2em", height: "2em" }} />
                </Link>
                {isAuthenticated ? (
                    <button onClick={handleLogout}>로그아웃</button>
                ) : (
                    <>
                        <button onClick={goLogin}>로그인</button>
                        <button onClick={goRegister}>회원가입</button>
                    </>
                )}
                <img onClick={goCart} src={cartImage} alt="cart" style={{ width: "2em", height: "2em" }} />
            </header>
        </div>
    );
}
export default RouterNav;
