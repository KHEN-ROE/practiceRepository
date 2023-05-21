import {useNavigate, Link} from 'react-router-dom';
import logoImage from '../images/ship.svg';
import cartImage from '../images/cart.svg';
import '../styles/RouterLogo.css';

const RouterLogo = () => {
    const navigate = useNavigate();

    const goCart = () => {
        navigate('/cart')
    };

    return(
        <div>
            <header className="rlogoheader">
                <Link to="/">
                    <img src={logoImage} alt="ship" style={{width : "2em", height : "2em"}} /> 
                </Link>
                <div className="rlogodiv2" onClick={goCart}>
                    <img className="rlogoimg2" src={cartImage} alt="cart"/>
                    <p className="rlogop1">관심상품</p>
                </div>
            </header>
        </div>
    );
}
export default RouterLogo;