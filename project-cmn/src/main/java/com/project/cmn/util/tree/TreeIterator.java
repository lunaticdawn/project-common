package com.project.cmn.util.tree;

import java.util.Iterator;
import java.util.Stack;

/**
 * TreeComponent 탐색을 위한 {@link Iterator}
 */
public class TreeIterator<E> implements Iterator<E> {
	private Stack<Iterator<E>> stack = new Stack<Iterator<E>>();

	/**
	 * 생성자
	 *
	 * @param iterator {@link Iterator}
	 */
	public TreeIterator(Iterator<E> iterator) {
		stack.push(iterator);
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		if (stack.isEmpty()) {
			return false;
		} else {
			Iterator<E> iterator = stack.peek();

			if (!iterator.hasNext()) {
				stack.pop();

				return hasNext();
			} else {
				return true;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E next() {
		if (hasNext()) {
			Iterator<E> iterator = stack.peek();
			TreeComponent treeComponent = (TreeComponent) iterator.next();

			if (treeComponent instanceof TreeComponent) {
				stack.push((Iterator<E>) treeComponent.iterator());
			}

			return (E) treeComponent;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
