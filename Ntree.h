// author: levitan

#ifndef NTREE_H
#define NTREE_H

#include <assert.h>
#include <list>
#include <map>
#include <numeric>
#include <queue>
#include <string>
#include <vector>

namespace std {
	std::string to_string(const std::string& str) { return str; }
}

template <class T>
class NTree {
	struct tnode {
		T data;
		tnode* parent;
		std::vector<tnode*> children;

		tnode (T x) : data(x), parent(NULL) {}

		void addChild(tnode* c) { children.push_back(c); }
	};

	tnode* root;

	// recursive private method called by >> operator
	std::queue<std::string> toStrings(tnode* nd) const {
		std::queue<std::string> rq;
		if (!nd) {	// base case: empty child: return empty vector
			return rq;
		}
		if (nd->children.size()==0) {	// base case: leaf: return vector with just that node's value
			rq.push(std::to_string(nd->data));
			return rq;
		}

		// get queue of strings representing each subtree
		std::vector<std::queue<std::string> > subtrees;
		for (tnode* c : nd->children) {
			subtrees.push_back(toStrings(c));
		}

		// add current value to return queue (will be at front)
		// pad to width of the next level
		int stringWidth = 0;
		int maxDepth = 0;
		for (auto st : subtrees) {
			stringWidth += st.front().length();
			maxDepth = std::max(maxDepth, (int)st.size());
		}
		int levelpad = (stringWidth + subtrees.size() - 1 - std::to_string(nd->data).length())/2;
		rq.push(std::string(levelpad, '_') + std::to_string(nd->data) +
				std::string(levelpad, '_'));

		// add space-padded string(s) to subtrees with fewer levels than max
		for (auto& st : subtrees) {
			int width = st.front().length();
			while (st.size() < maxDepth) {
				st.push(std::string(width, '_'));
			}
		}

		// combine subtrees at each level
		while (!subtrees.front().empty()) {		// all subtrees now have same depth
			std::vector<std::string> levelstrings;	// hold strings at this level
			for (auto& st : subtrees) {
				levelstrings.push_back(st.front());
				st.pop();
			}
			
			// levelstrings now contains string from each subtree
			// combine into single string and add to rq
			std::string levelstr = std::accumulate(levelstrings.begin(),
					levelstrings.end(), std::string(),
					[](const std::string& a, const std::string& b) -> std::string {
							return a + (a.length() > 0 ? " " : "") + b;
					});
			rq.push(levelstr);

		}	// iterate to next level

		// all subtrees have been processed
		return rq;
	}

	std::string toString(tnode* nd) const {
		std::queue<std::string> q = toStrings(nd);
		std::string str;
		while (!q.empty()) {
			str += (q.front() + "\n");
			q.pop();
		}
		return str;
	}

        bool treeEqual(tnode* lnd, tnode* rnd) {
          if (lnd->data != rnd->data) return false;
          if (lnd->children.size() != rnd.children->size()) return false;
          for (int i = 0; i < lnd->children.size(); i++) {
            if (!treeEqual(lnd->children[i], rnd->children[i])) return false;
          }
          return true;
        }

	public:
	NTree() : root(NULL) {}

	NTree(const std::vector<T>& values, const std::vector<int>& parents) {
		if (values.size() != parents.size()) {
			std::cout << "Error: values has " << values.size() << " elements and parents has " << parents.size() << " elements.\n";
			throw std::exception();
		}

		std::map<T, tnode*> valmap;	// will map values to node addresses
		for (int i = 0; i < values.size(); i++) {
			tnode* nd = new tnode(values[i]);
			valmap[values[i]] = nd;
			if (parents[i] >= 0) {		// -1 signals root
				nd->parent = valmap[values[parents[i]]];
				nd->parent->addChild(nd);
			}
			else root = nd;
		}
			
	}

	~NTree() {
		if (!root) return;
		std::queue<tnode*> q;
		q.push(root);
	
		while (!q.empty()) {
			tnode* nd = q.front();
			q.pop();
			for (tnode* c : nd->children) q.push(c);
			delete nd;
		}
	}

	friend std::ostream& operator<<(std::ostream& stream, const NTree& tree) {
		stream << tree.toString(tree.root);
		return stream;
	}

	bool operator==(const NTree<T>& rhs) {
		tnode *lnd = root, *rnd = rhs.root;
                if (!(lnd || rnd)) return true;
                if (!lnd || !rnd) return false;
                return treeEqual(lnd, rnd);
	}

	// TODO: implement method to write tree in recoverable format to file
	void serialize(const std::string& filename) {
		
	}

	// TODO: implement method to read tree in from file
	void deserialize(const std::string& filename) {

	}

};

#endif
