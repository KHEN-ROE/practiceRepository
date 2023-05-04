import {BrowserRouter, Route, Routes, useNavigate, Link} from 'react-router-dom';
import logoImage from '../images/ship.svg';
import cartImage from '../images/cartlist.svg';

const RouterNav = () => {
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

    return(
        <div>
            <header style={{background: 'lightgray', padding: 16, fontSize:24}}>
                <Link to="/">
                    <img src={logoImage} alt="ship" style={{width : "2em", height : "2em"}} /> 
                </Link>
                <button onClick={goLogin}>로그인</button>
                <button onClick={goRegister}>회원가입</button>
                <img onClick={goCart} src={cartImage} alt="cart" style={{width : "2em", height : "2em"}}/>
            </header>
        </div>
    );
}
export default RouterNav;
