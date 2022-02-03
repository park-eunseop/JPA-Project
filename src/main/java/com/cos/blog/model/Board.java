package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //빌더패턴
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable=false,length=100)
	private String title;
	
	@Lob  //대용량 데이터
	private String content; //섬머노트 라이브러리
	
	@ColumnDefault("0")
	private int count;  //조회수
	
	@CreationTimestamp
	private Timestamp createDate;
	
	@ManyToOne(fetch = FetchType.EAGER) //Many=Board, User=one, 기본 전략(board를 select 하면 user는 가져올께 one이니깐)
	@JoinColumn(name = "userId")
	private User user;  //DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다.
	
	//OneToMany는 fetch 전략이 Eager가 아니야 Many니깐 기본이 LAZY인데 한번에 다 가져오는거면 EAGER로 해야해
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER) // mappedBy 연관관계의 주인이 아니다(난 FK가 아니에요), DB에 컬럼을 만들지마
	private List<Reply> reply;
}
