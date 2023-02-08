package com.project.cmn.util.tree;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 트리 구조의 자료구조를 만들기 위한 DTO 객체
 */
@Getter
@Setter
public class TreeDto {

	/**
	 * 노드의 아이디
	 */
	private String id;

	/**
	 * 부모 노드의 아이디
	 */
	private String parentId;

	/**
	 * 자식 노드들
	 */
	private List<TreeDto> children;

	/**
	 * 자식 노드를 추가
	 *
	 * @param treeDto {@link TreeDto}
	 */
	public void add(TreeDto treeDto) {
		if (children == null) {
			children = new ArrayList<>();
		}

		children.add(treeDto);
	}
}
