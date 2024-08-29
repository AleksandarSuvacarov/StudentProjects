import math


class Heuristic:
    def get_evaluation(self, state):
        pass


class ExampleHeuristic(Heuristic):
    def get_evaluation(self, state):
        return 0


class Hamming(Heuristic):
    def get_evaluation(self, state):
        wrong_tiles = 0
        for i in range(len(state)):
            if state[i] == 0:
                continue
            if i + 1 != state[i]:
                wrong_tiles = wrong_tiles + 1
        return wrong_tiles


class Manhattan(Heuristic):
    def get_evaluation(self, state):
        distance = 0
        divider = int(math.sqrt(len(state)))
        for i in range(len(state)):
            if state[i] == 0:
                continue
            x_i = i // divider
            y_i = i % divider
            x_state = (state[i] - 1 if state[i] != 0 else len(state) - 1) // divider
            y_state = (state[i] - 1 if state[i] != 0 else len(state) - 1) % divider
            loc_dist = abs(x_i - x_state) + abs(y_i - y_state)
            distance = distance + loc_dist
        return distance
