package com.example.tstoretest.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by 재화 on 2016-05-03.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
    String productId;
    String webUrl;
    String categoryName;
    int charge;
    int downloadCount;
    String description;
    String thumbnailUrl;
    String name;
    String categoryCode;
    float score;
    String tinyUrl;
}
