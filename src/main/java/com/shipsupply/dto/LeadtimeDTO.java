package com.shipsupply.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeadtimeDTO {
    private Long leadtime;
    private Date date;

    // 객체의 동일성 검증. 이게 있어야 ItemRepository에서 중복제거 할 수 있음
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 현재객체(this)와 전달받은 객체 o를 비교
        if(o == null || getClass() != o.getClass()) return false; // 매개변수로 전달받은 값이 null이거나, 현재 객체와 같은 타입이 아닌 경우 false
        LeadtimeDTO that = (LeadtimeDTO) o;
        return Objects.equals(leadtime, that.leadtime) && // 현재 객체의 leadtime,date 필드가 매개변수로 전달된 필드외 같은지 검증
                Objects.equals(date, that.date); // 같으면 true, 아니면 false
    }

    @Override
    public int hashCode() {
        return Objects.hash(leadtime, date); // 객체의 해시코드 반환
    }

}
