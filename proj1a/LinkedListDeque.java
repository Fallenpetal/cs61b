public class LinkedListDeque<T> {

	public class LinkNode<T> {
		public T data;
		public LinkNode prior;
		public LinkNode next;

		 public LinkNode(T value,LinkNode prev_pointer,LinkNode next_pointer){
		 	data = value;
		 	prior = prev_pointer;
		 	next = next_pointer;
		 }

		public LinkNode(T value){
			data = value;
		}
	}

	public LinkNode sentinel;
	public int size;

	public LinkedListDeque(){
		sentinel = new LinkNode<>(999);
		sentinel.prior = sentinel;
		sentinel.next = sentinel;
		size = 0;
	}

	// public void addFirst(T item){
	// 	sentinel.next.prior = new LinkNode<>(item,sentinel,sentinel.next);
	// 	sentinel.next=sentinel.next.prior;
	// 	size = size+1;
	// }

	public void addFirst(T item){
		LinkNode p = new LinkNode<>(item);
		p.next = sentinel.next;
		p.prior = sentinel;
		sentinel.next.prior = p;
		sentinel.next = p;
		size = size+1;
	}

	public void addLast(T item){
		LinkNode p = new LinkNode<>(item);
		p.next = sentinel.prior.next;
		p.prior = sentinel.prior;
		sentinel.prior.next = p;
		sentinel.prior = p;
		size = size+1;
	}

	public boolean isEmpty(){
		if(size == 0)
			return true;
		else
			return false;
	}

	public int size(){
		return size;
	}

	public void printDeque(){
		LinkNode p = sentinel.next;
		while(p!=sentinel){
			System.out.print(p.data+" ");
			p = p.next;
		}
	}

	public T removeFirst(){
		LinkNode p = sentinel.next;
		if(p == sentinel){
			return null;
		}
		T temp = (T)p.data;
		p.next.prior = sentinel;
		sentinel.next = p.next;
		size = size-1;
		return temp;
	}

	public T removeLast(){
		LinkNode p =sentinel.prior;
		if(p == sentinel){
			return null;
		}
		T temp = (T)p.data;
		p.prior.next = sentinel;
		sentinel.prior = p.prior;
		size = size-1;
		return temp;
	}

	public T get(int index){
		LinkNode p = sentinel.next;
		int flag = 0;
		while(p != sentinel){
			if(flag == index){
				return (T) p.data;
			}
			flag++;
			p = p.next;
		}
		return null;

	}

//	public T getRecursive(int index){
//
//	}

}
