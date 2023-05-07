import React, { useEffect, useState } from 'react'
import jwt_decode from 'jwt-decode';

const Tform = () => {
  const [todos, setTodos] = useState([]); // 입력값을 담을 배열
  const [newTodo, setNewTodo] = useState('');
  const [idToken, setIdToken] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userEmail, setUserEmail] = useState('');
  const [decodedToken, setDecodedToken] = useState('');

  useEffect(() => {
    console.log("useEffect 실행");
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");
    if (token) { //token이 null이 아니면
      setIdToken(token);
      console.log("토큰: ", token);
      setIsLoggedIn(true);
    }
  }, []);

  // idToken 상태 변경에 대한 useEffect 추가
  useEffect(() => {
    if (idToken) {
      const decoded = jwt_decode(idToken);
      setDecodedToken(decoded);
      setUserEmail(decoded.sub);
      console.log("인증 토큰:", idToken);
    }
  }, [idToken]);

  useEffect(() => { // get 요청. 이때는 method, headers, body 안써도 됨
    if (isLoggedIn && userEmail) {
      fetch(`/api/todos?email=${encodeURIComponent(userEmail)}`, {
        headers: {
          Authorization: `Bearer ${idToken}`,
        },
      })
        .then((response) => response.json()) // 응답을 받아서 제이슨 형식으로 바꾸고?
        .then((data) => setTodos(data)); //받은 data를 setTodos에 파라미터로 전달
    }
  }, [isLoggedIn, userEmail, idToken]);

  //post요청으로 user 테이블에 삽입
  useEffect(() => {
    console.log("isLoggedIn:", isLoggedIn);
    console.log("userEmail:", userEmail);
    console.log("idToken:", idToken);
    console.log("decodedToken:", decodedToken);

    if (isLoggedIn && userEmail && decodedToken) {
      // 사용자 정보 추출
      const userInfo = {
        email: decodedToken.sub,
        name: decodedToken.name,
        locale: decodedToken.locale,

      };
      console.log("전송할 사용자 정보:", userInfo);

      fetch('/addUser', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${idToken}`,
        },
        body: JSON.stringify(userInfo),

      });
    }
  }, [isLoggedIn, userEmail, idToken, decodedToken]);

  function addTodo(e) {
    console.log("newtodo", newTodo)
    e.preventDefault(); // 새로고침 등 방지
    if(newTodo.trim() !== ''){ // 공백이 아닐 때만 post요청
    fetch('/api/todos', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${idToken}`,
      },
      body: JSON.stringify({ text: newTodo, user: { email: userEmail } }) //JSON 형태로 변환해서 서버에 전달
    })
      .then(response => response.json()) // 새로운 newTodo를 서버로 보내고, 서버로부터 응답을 받아와서 화면에 렌더링. 
      // 서버로부터 응답을 받으면, response.json() 함수 사용해서 응답데이터를 자.스 객체로 변환 
      .then(data => { // 변환된 데이터는 data에 저장
        setTodos([...todos, data]); //... 연산자 - 기존 데이터에 새로운 데이터(newTodo) 더하는 새로운 배열 생성
        setNewTodo(''); // newTodo 변수를 빈 문자열로 설정. 웹페이지에서 input field를 clear시킴.
        // 그니까, 제출하면 setTodo에는 기존 데이터 + 제출한 데이터(newTodo) 포함된 새 배열 생성하고, setNewTodo 한번 더 호출해서 input field를 clear시킴
      });
    }
  }

  const deleteTodo = (id) => {
    fetch(`/api/todos/${id}?email=${encodeURIComponent(userEmail)}`, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${idToken}`,
      },
    })
      .then(response => {
        console.log("response", response);
        const updatedTodos = todos.filter(todo => todo.id !== id);
        setTodos(updatedTodos);
      });
  }


  const editTodo = (id) => {
    console.log("id to edit:", id)
    const updatedText = prompt("Enter updated text:");
    if (updatedText) {
      fetch(`/api/todos/${id}?email=${encodeURIComponent(userEmail)}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${idToken}`,
        },
        body: JSON.stringify({ text: updatedText })
      })
        .then(response => response.json())
        .then(data => {
          const updatedTodos = todos.map(todo => {
            if (todo.id === id) {
              return { ...todo, text: data.text };
            } else {
              return todo;
            }
          });
          setTodos(updatedTodos);
        });
    }
  }

  return (
    <div className='form'>
      {isLoggedIn ? (
        // 로그인한 사용자에게만 보이는 컴포넌트
        <div>
          <form onSubmit={addTodo}>
            <input type="text" value={newTodo} onChange={e => setNewTodo(e.target.value)} placeholder='                                                                    Stop Everything, Do Onething' />
            <button type="submit">Add Todo</button>
            {/* 로그아웃 버튼 추가 */}
            <button onClick={() => { window.location.href = 'http://localhost:8080/logout'; }}>Logout</button>
          </form>
          <div className='list'>
            {todos.map(todo => (
              <li key={todo.id}>{todo.text}
                <button type='submit' onClick={() => editTodo(todo.id)}>edit</button>
                <button type='submit' onClick={() => deleteTodo(todo.id)} >delete</button>
              </li>
            ))}
          </div>

        </div>
      ) : (
        // 로그인하지 않은 사용자에게 보이는 컴포넌트
        <div className='logbutton'>
          <button onClick={() => { window.location.href = 'http://localhost:8080/oauth2/authorization/google'; }}>Login with Google</button>
        </div>
      )}
    </div>
  );
}

export default Tform
