import random
import time

import config


class Algorithm:
    def __init__(self, heuristic=None):
        self.heuristic = heuristic
        self.nodes_evaluated = 0
        self.nodes_generated = 0
        self.queue = []
        self.visited = set()

    def get_legal_actions(self, state):
        self.nodes_evaluated += 1
        max_index = len(state)
        zero_tile_ind = state.index(0)
        legal_actions = []
        if 0 <= (up_ind := (zero_tile_ind - config.N)) < max_index:
            legal_actions.append(up_ind)
        if 0 <= (right_ind := (zero_tile_ind + 1)) < max_index and right_ind % config.N:
            legal_actions.append(right_ind)
        if 0 <= (down_ind := (zero_tile_ind + config.N)) < max_index:
            legal_actions.append(down_ind)
        if 0 <= (left_ind := (zero_tile_ind - 1)) < max_index and (left_ind + 1) % config.N:
            legal_actions.append(left_ind)
        return legal_actions

    def apply_action(self, state, action):
        self.nodes_generated += 1
        copy_state = list(state)
        zero_tile_ind = state.index(0)
        copy_state[action], copy_state[zero_tile_ind] = copy_state[zero_tile_ind], copy_state[action]
        return tuple(copy_state)

    def get_steps(self, initial_state, goal_state):
        pass

    def get_solution_steps(self, initial_state, goal_state):
        begin_time = time.time()
        solution_actions = self.get_steps(initial_state, goal_state)
        print(f'Execution time in seconds: {(time.time() - begin_time):.2f} | '
              f'Nodes generated: {self.nodes_generated} | '
              f'Nodes evaluated: {self.nodes_evaluated}')
        return solution_actions

    class Node:
        def __init__(self, state, actions=None):
            if actions is None:
                actions = []
            self.state = state
            self.actions = actions
            self.parent = None
            self.last_pos = -1
            self.rate = 100000
            self.visited = set()

    def put_in_queue(self, new_node):
        # self.queue.append(new_node)
        # self.queue.sort(key=lambda x: (x.rate, x.state))
        # return
        old_len = len(self.queue)
        for i in range(len(self.queue)):
            if self.queue[i].rate < new_node.rate:
                continue
            elif self.queue[i].rate > new_node.rate:
                self.queue.insert(i, new_node)
                break
            elif self.queue[i].rate == new_node.rate:
                if self.queue[i].state > new_node.state:
                    self.queue.insert(i, new_node)
                    break
        if len(self.queue) == old_len:
            self.queue.append(new_node)

        return


class ExampleAlgorithm(Algorithm):
    def get_steps(self, initial_state, goal_state):
        state = initial_state
        solution_actions = []
        while state != goal_state:
            legal_actions = self.get_legal_actions(state)
            action = legal_actions[random.randint(0, len(legal_actions) - 1)]
            solution_actions.append(action)
            state = self.apply_action(state, action)
        return solution_actions


class BreadthFirstSearch(Algorithm):

    def get_steps(self, initial_state, goal_state):
        node = self.Node(initial_state)
        flag = 0
        self.queue.append(node)
        solution_actions = []
        while True:
            curr = self.queue.pop(0)
            legal_actions = self.get_legal_actions(curr.state)
            try:
                legal_actions.remove(curr.last_pos)
            except ValueError:
                pass

            for action in legal_actions:
                new_state = self.apply_action(curr.state, action)
                if new_state in self.visited:
                    continue
                self.visited.add(new_state)

                new_node = self.Node(new_state)
                new_node.actions = curr.actions.copy()
                new_node.actions.append(action)
                new_node.last_pos = curr.state.index(0)

                if new_state == goal_state:
                    solution_actions = new_node.actions
                    flag = 1
                    break

                self.queue.append(new_node)
            if flag == 1:
                break
        return solution_actions


class BestFirstSearch(Algorithm):

    def get_steps(self, initial_state, goal_state):
        node = self.Node(initial_state)
        self.queue.append(node)
        solution_actions = []
        while True:
            curr = self.queue.pop(0)
            if curr.state == goal_state:
                solution_actions = curr.actions
                break

            legal_actions = self.get_legal_actions(curr.state)
            try:
                legal_actions.remove(curr.last_pos)
            except ValueError:
                pass

            for action in legal_actions:
                new_state = self.apply_action(curr.state, action)

                if new_state in self.visited:
                    continue
                self.visited.add(new_state)

                new_node = self.Node(new_state)
                new_node.rate = self.heuristic.get_evaluation(new_state)
                new_node.last_pos = curr.state.index(0)
                new_node.actions = curr.actions.copy()
                new_node.actions.append(action)

                self.put_in_queue(new_node)

        return solution_actions


class AStarSearch(Algorithm):

    def get_steps(self, initial_state, goal_state):
        node = self.Node(initial_state)
        self.queue.append(node)
        solution_actions = []
        while True:
            curr = self.queue.pop(0)

            if curr.state == goal_state:
                solution_actions = curr.actions
                break

            legal_actions = self.get_legal_actions(curr.state)
            try:
                legal_actions.remove(curr.last_pos)
            except ValueError:
                pass

            for action in legal_actions:
                new_state = self.apply_action(curr.state, action)
                if new_state in self.visited:
                    continue
                self.visited.add(new_state)

                new_node = self.Node(new_state)
                new_node.actions = curr.actions.copy()
                new_node.actions.append(action)
                new_node.rate = self.heuristic.get_evaluation(new_state)
                new_node.rate = new_node.rate + len(new_node.actions)
                new_node.last_pos = curr.state.index(0)

                self.put_in_queue(new_node)

        return solution_actions
