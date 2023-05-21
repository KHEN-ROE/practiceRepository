import { useNavigate } from 'react-router-dom';
import '../styles/MainPage.css';

const MainPage = () => {
    const navigate = useNavigate();

    const goProductSelect = () => {
        navigate("/productselect");
    };

   


    return (
        <div className="maindiv1">
            <div className="maindiv2">
                <h1>MainPage</h1>
                <div>서비스 내용</div>
                <button onClick={goProductSelect}>바로가기</button>
            </div>
            
        </div>
    );
}
export default MainPage;
