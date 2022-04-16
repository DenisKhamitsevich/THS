package com.market.ths.item;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table(name = "appitem")
public class Item {
    @Id
    @SequenceGenerator(name="id_seq", sequenceName="id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_seq")
    private Long id;
    private byte[] image;
    private String title;
    private String description;
    private Double price;

    public Item(byte[] image, String title, String description, Double price) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", image=" + Arrays.toString(image) +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
