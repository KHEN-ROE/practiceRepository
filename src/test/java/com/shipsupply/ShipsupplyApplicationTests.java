package com.shipsupply;

import com.shipsupply.domain.*;
import com.shipsupply.persistence.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ShipsupplyApplicationTests {

//    @Autowired
//    ItemRepository ir;

//    @Test
//    void contextLoads() {
//    }

//    @Test
//    public void addItem() {
//        Item i = new Item();
//        i.setItem("dd");
//        i.setAssembly("dd");
//        i.setMachinery("dd");
//        i.setCompany("dd");
//        i.setPartNo1("dd");
//        i.setLeadtime(10L);
//        ir.save(i);
//    }


    // Board 객체가 같은 seq를 가지고 있을 때, equals, hashcode메서드가 정삭적으로 동작하는지 검증
//    @Test
//    void testEquality() {
//        User user1 = new User("testId", "testPassWord", "newpwd", "conpwd",  "testEmail", "testUsername", "testRole", "testProvider", "testProviderId" ,false);
//        User user2 = new User("testId", "testPassWord", "newpwd", "conpwd",  "testEmail", "testUsername", "testRole", "testProvider", "testProviderId" ,false);
//
//
//        Board board1 = new Board(1L, "title", "text", new Date(), user1);
//        Board board2 = new Board(1L, "title", "text", new Date(), user1);
//
//        Comment comment1 = new Comment(1L, "text", new Date(), 0L, user1, board1);
//        Comment comment2 = new Comment(1L, "text", new Date(), 0L, user1, board1);
//
//        Hit hit1 = new Hit(0L, user1, board1, comment1);
//        Hit hit2 = new Hit(0L, user1, board1, comment1);
//
//        Item item1 = new Item(0L, "test", "test", "test",  "test", 0L, "test", "test");
//        Item item2 = new Item(0L, "test", "test", "test",  "test", 0L, "test", "test");
//
//
//        assertEquals(user1, user2);
//        assertEquals(user1.hashCode(), user2.hashCode());
//
//        assertEquals(item1, item2);
//        assertEquals(item1.hashCode(), item2.hashCode());
//
//        assertEquals(hit1, hit2);
//        assertEquals(hit1.hashCode(), hit2.hashCode());
//
//        assertEquals(comment1, comment2);
//        assertEquals(comment1.hashCode(), comment2.hashCode());
//
//        assertEquals(board1, board2);
//        assertEquals(board1.hashCode(), board2.hashCode());
//    }

}
