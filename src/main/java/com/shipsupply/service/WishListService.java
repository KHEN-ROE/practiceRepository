package com.shipsupply.service;

import com.shipsupply.domain.User;
import com.shipsupply.domain.WishList;
import com.shipsupply.persistence.UserRepository;
import com.shipsupply.persistence.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    @Autowired
    WishListRepository wishListRepository;

    @Autowired
    UserRepository userRepository;

    public List<WishList> getList(String userId) {
        return wishListRepository.findByUserId(userId);
    }

    public Optional<WishList> getDetail(Long id) {
        return wishListRepository.findById(id);
    }

    public WishList addList(WishList wishList) {
        return wishListRepository.save(wishList);
    }

    public WishList updateList(WishList wishList) { // 필드를 안 보내면 db에서 null로 바뀜 -> 해결
        Optional<WishList> findWishList = wishListRepository.findById(wishList.getId());
        if(findWishList.isPresent()) {
            WishList w = findWishList.get();
            Optional.ofNullable(wishList.getCompany()).ifPresent(w::setCompany);
            Optional.ofNullable(wishList.getItem()).ifPresent(w::setItem);
            return wishListRepository.save(w);
        }
        return null;
    }

    public void deleteList(WishList wishList) {
        Optional<WishList> findWishList = wishListRepository.findById(wishList.getId());
        if(findWishList.isPresent()) {
            WishList w = findWishList.get();
            wishListRepository.delete(w);
        }
    }
}
