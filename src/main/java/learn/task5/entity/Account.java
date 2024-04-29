package learn.task5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
 public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Getter
    @Setter
    @Column(name = "account_pool_id")
    Integer account_pool_id;
    @Getter
    @Setter
    @Column(name = "account_number")
    String account_number;
    @Getter
    @Setter
    @Column(name = "bussy")
    boolean bussy;
}
