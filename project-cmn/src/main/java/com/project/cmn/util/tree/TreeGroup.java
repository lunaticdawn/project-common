package com.project.cmn.util.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 자식을 가진 노드 객체
 */
@Getter
@RequiredArgsConstructor
public class TreeGroup extends TreeComponent {

	private static final long serialVersionUID = 1L;

	private final TreeDto info;
	private List<TreeComponent> children;

	@Override
	public void add(TreeComponent treeComponent) {
		if (children == null) {
			children = new ArrayList<>();
		}

		children.add(treeComponent);
		info.add(treeComponent.getInfo());
	}

	@Override
	public Iterator<TreeComponent> iterator() {
		return new TreeIterator<TreeComponent>(children.iterator());
	}

	@Override
	public String getShowHtml() {
		return super.getShowHtml();
	}
}
