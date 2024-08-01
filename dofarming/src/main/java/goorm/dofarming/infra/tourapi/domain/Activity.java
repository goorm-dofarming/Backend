package goorm.dofarming.infra.tourapi.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import goorm.dofarming.domain.jpa.like.entity.Like;
import goorm.dofarming.domain.jpa.location.entity.Location;
import goorm.dofarming.domain.jpa.user.entity.Role;
import goorm.dofarming.domain.jpa.user.entity.User;
import goorm.dofarming.global.common.entity.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "3")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends Location {

    public Activity(String title, String image, String addr, String tel, double mapx, double mapy) {
        super(title, image, addr, tel, mapx, mapy);
    }
}
