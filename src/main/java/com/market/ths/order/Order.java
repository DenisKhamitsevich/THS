package com.market.ths.order;

import com.market.ths.item.Item;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "apporder")
public class Order {
    @Id
    @SequenceGenerator(name="id_seq", sequenceName="id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="id_seq")
    @Column(name = "order_id")
    private Long id;
    private Long userID;
    private String time;
    @OneToMany
    private List<Item> items;

    public Order(Long userID, List<Item> itemsID,String time) {
        this.userID = userID;
        this.items = itemsID;
        this.time=time;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userID=" + userID +
                ", time='" + time + '\'' +
                ", items=" + items +
                '}';
    }
}
