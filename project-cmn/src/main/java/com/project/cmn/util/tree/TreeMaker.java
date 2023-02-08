package com.project.cmn.util.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * DB에서 조회한 리스트를 Tree로 만들어준다.
 */
public class TreeMaker {

	/**
	 * DB에서 조회한 리스트 재귀호출을 통하여 Tree 형태로 만든다.
	 *
	 * @param infoList DB에서 조회한 리스트
	 * @param rootComponent 최상위 노드
	 */
	private void addItem(List<? extends TreeDto> infoList, TreeComponent rootComponent) {
		List<TreeDto> remainInfoList = new ArrayList<>();
		Iterator<TreeComponent> iter = rootComponent.iterator();

		TreeComponent parent = null;
		TreeComponent child = null;

		if (iter.hasNext()) {
			while (iter.hasNext()) {
				parent = iter.next();

				if (parent instanceof TreeGroup) {
					if (!parent.iterator().hasNext()) {
						break;
					}
				}
			}
		} else {
			parent = rootComponent;
		}

		if (parent != null && !parent.iterator().hasNext()) {
			for (TreeDto info : infoList) {
				if (StringUtils.equals(info.getParentId(), parent.getInfo().getId())) {
					if (isGroup(infoList, info.getId())) {
						child = new TreeGroup(info);
					} else {
						child = new TreeItem(info);
					}

					parent.add(child);
				} else {
					remainInfoList.add(info);
				}
			}

			if (infoList.size() != remainInfoList.size()) {
				addItem(remainInfoList, rootComponent);
			}
		}
	}

	/**
	 * 부모 노드인지 판단한다.
	 *
	 * @param infoList Tree로 만들 리스트
	 * @param id 노드의 아이디
	 * @return 그룹 여부. true : Group, false : Item
	 */
	private boolean isGroup(List<? extends TreeDto> infoList, String id) {
		boolean isGroup = false;

		for (TreeDto info : infoList) {
			if (StringUtils.equals(info.getParentId(), id)) {
				isGroup = true;

				break;
			}
		}

		return isGroup;
	}

	/**
	 * Tree 형태의 정보를 담고 있는 목록을 가져온다.
	 *
	 * @param infoList DB에서 조회한 리스트
	 * @return Tree 형태의 정보를 담고 있는 목록
	 */
	public List<?> getList(List<? extends TreeDto> infoList) {
		TreeDto treeDto = new TreeDto();

		treeDto.setId("0");

		TreeGroup rootComponent = new TreeGroup(treeDto);

		this.addItem(infoList, rootComponent);

		return rootComponent.getInfo().getChildren();
	}
}
