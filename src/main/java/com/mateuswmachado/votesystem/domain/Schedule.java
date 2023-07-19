package com.mateuswmachado.votesystem.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer yesVote;
    private Integer noVote;
    private String subject;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<User> votes;
    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private VoteSession voteSession;
}
