class node():
    def __init__(self, data):
        self.data = data
        self.children = []
        self.parent = None
        self.spisak = []
        self.valid = 0

    def add_child(self, child):
        child.parent = self
        self.children.append(child)


if __name__ == '__main__':

    def postorder(root):
        stack = [root]
        niz = []
        while len(stack) != 0:
            a = stack[len(stack) - 1]
            stack.pop(len(stack) - 1)
            niz.insert(0, a.data)
            for i in a.children:
                stack.append(i)
        return niz

    n1 = node("A")
    n2 = node("B")
    n3 = node("C")
    n4 = node("D")
    n5 = node("E")
    n6 = node("F")
    n7 = node("G")
    n8 = node("H")
    n9 = node("I")
    n10 = node("J")
    n11 = node("K")
    n12 = node("L")
    n13 = node("M")
    n14 = node("N")
    n1.children = [n2, n3, n4]
    n2.children = [n5, n6, n7]
    n3.children = [n8, n9]
    n4.children = [n10, n11, n12]
    n6.children = [n13, n14]

    print(postorder(n1))
