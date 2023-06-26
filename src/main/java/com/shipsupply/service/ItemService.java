package com.shipsupply.service;

import com.shipsupply.domain.Item;
import com.shipsupply.dto.*;
import com.shipsupply.persistence.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    //모든 아이템 리스트 출력
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    // 검색창에서 카테고리만 출력
    public List<CategoryDTO> getCategoriesByKeyword(String category) {
        return itemRepository.findByCategoryLike(category);
    }

    // 검색창에서 발주처만 출력
    public List<CompanyDTO> getCompaniesByKeyword(String company) {
        return itemRepository.findByCompanyLike(company);
    }

    // 검색창에서 부품명만 출력
    public List<ItemDTO> getItemByKeyword(String item) {
        return itemRepository.findByItemLike(item);
    }

    // 특정 카테고리를 포함하는 모든 행 출력
    public List<Item> findByCategory(String category) {
        return itemRepository.findByCategoryContaining(category);
    }

    // 특정 부품명 포함하는 모든 행 출력
    public List<Item> findByItem(String item) {
        return itemRepository.findByItemContaining(item);
    }

    // 특정 발주처 포함하는 모든 행 출력
    public List<Item> findByCompany(String company) {
        return itemRepository.findByCompanyContaining(company);
    }

    // 과거 리드타임 추이
    public List<LeadtimeDTO> getPastLeadtime(String item, String category, String company) {
        List<LeadtimeDTO> getList = itemRepository.findByItemAndCategoryAndCompanyOrderByDate(item, category,  company);
        log.info("getList : " + getList);
        return getList;
    }

    // 리드타임 분포
    public List<LeadtimeDistributionDTO> getLeadtimeDistribution() {
        return itemRepository.findByLeadtimeAndCount();
    }

    // 카테고리 분포
    public List<CategoryDistributionDTO> getCategoryDistribution() {
        return itemRepository.findByCategoryAndCountOrderByCategoryCountDesc();
    }

    // 카테고리 예측
    public String predCategory(Map<String, String> data) {

        String flaskUrl = "http://15.164.163.107:5000/api/item/predict/classify";

        // RestTemplate 객체 생성
        // HTTP 통신을 위한 클래스
        RestTemplate restTemplate = new RestTemplate();

        // HTTP Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map= new HashMap<>();
        map.put("a", data.get("Machinery"));
        map.put("b", data.get("Assembly"));
        map.put("c", data.get("PartNo1"));
        map.put("d", data.get("Item"));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        // RestTemplate은 스프링에서 다른 서버로 HTTP요청을 보내고 응답을 받기 위한 클래스
        // restTemplate을 이용하여 POST 요청을 보내고(postForEntity) 결과를 POJO(Plain Old Java Object)로 반환.
        // 받은 POJO를 자동으로 자바 객체로 변환해줌
        // POJO(Plain Old Java Object)는 특정 규약, 프레임워크, 기술 등에 종속되지 않는 일반적인 Java 객체
        // String.class -> 요청의 응답 데이터가 String 타입
        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);

        return response.getBody();
    }

    public String predLeadtime(Map<String, String> data) {

        String flaskUrl = "http://15.164.163.107:5000/api/item/predict/regression";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("a", data.get("subject"));
        map.put("b", data.get("ship"));
        map.put("c", data.get("key2"));
        map.put("d", data.get("assembly"));
        map.put("e", data.get("currency"));
        map.put("f", data.get("company"));
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(flaskUrl, request, String.class);
        log.info("리드타임 : " + response);

        return response.getBody();
    }

}
