package com.project.cmn.util.tree;

import java.util.Iterator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 자식 노드 객체
 */
@Getter
@RequiredArgsConstructor
public class TreeItem extends TreeComponent {

	private static final long serialVersionUID = 1L;

	private final TreeDto info;

	@Override
	public Iterator<TreeComponent> iterator() {
		return new NullIterator<TreeComponent>();
	}

	@Override
	public String getShowHtml() {
		return super.getShowHtml();
	}
}
