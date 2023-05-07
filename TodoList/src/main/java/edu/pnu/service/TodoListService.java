package edu.pnu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.TodoList;
import edu.pnu.domain.User;
import edu.pnu.persistence.TodoListRepository;
import edu.pnu.persistence.UserRepository;

@Service
public class TodoListService {
	
	@Autowired
	TodoListRepository tr;

	@Autowired
	UserRepository ur;
	
	public List<TodoList> getLists(String email){
		Optional<User> findUser = ur.findByEmail(email);
		if(findUser.isPresent()) {
			User u = findUser.get();
			if(u.getEmail().equals(email)) {
				return tr.findByUser(u);
			}else {
				throw new RuntimeException("해당 아이디를 가진 회원이 없습니다.");
			}
		}
		return null;
	}
	
	public TodoList addList(TodoList todolist) {
		String email = todolist.getUser().getEmail(); // 클라이언트에서 전달된 email을 가져옴
		Optional<User> findUser = ur.findByEmail(email); // UserRepository를 사용하여 email로 User를 조회.
		if(findUser.isPresent()) {
			User user = findUser.get(); // 조회된 사용자 엔티티를 가져옴.
			todolist.setUser(user); // user 엔티티와 todolist 엔티티의 관계를 설정해줌.
									// db에 저장될 때 todolist테이블의 user컬럼에 해당 사용자의 email을 외래키로 저장
		}else {
			throw new RuntimeException("해당 아이디를 가진 회원이 없습니다.");
		}
		
		System.out.println("Saving todo item: " + todolist);
		
	    return tr.save(todolist);
	}
	
	public TodoList updateList(TodoList todolist, Long id, String email) {
		Optional<User> findUser = ur.findByEmail(email);
		if(findUser.isPresent()) {
			User u = findUser.get(); // 해당유저 정보 삽입
			if(u.getEmail().equals(email)) { // 정보 일치하는지 검사. 일치하면~
				TodoList tl = tr.findById(id).get();
				tl.setText(todolist.getText());
				System.out.println("수정할 목록 : " + todolist);
				return tr.save(tl);
			}
		}else {
			throw new RuntimeException("권한이 없습니다.");
		}
		return null;
	}
	
	public void deleteList(Long id, String email) {
		Optional<User> findUser = ur.findByEmail(email);
		if(findUser.isPresent()) {
			User u = findUser.get();
			if(u.getEmail().equals(email)) {
				System.out.println("삭제할 목록의 id:" + id);
				tr.deleteById(id);
			}
		}
		
	}
}
