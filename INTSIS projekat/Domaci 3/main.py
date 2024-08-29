import config
import environment
from environment import Environment, Quit
import random
import numpy as np
from matplotlib import pyplot
import seaborn
import pandas

env = Environment(f'maps/map.txt')
mapa = env.get_field_map()
env_size = len(mapa[0])
observation_space = len(mapa) * env_size
action_space = 4


def get_action_eps_greedy_policy(env, q_tab, st, eps):
    prob = random.uniform(0, 1)
    return env.get_all_actions()[np.argmax(q_tab[st])] if prob > eps else env.get_random_action()

def train(num_episodes, max_steps, lr, gamma, eps_min, eps_max, eps_dec_rate, env):
    avg_returns = []
    avg_steps = []
    q_tab = np.zeros((observation_space, action_space))
    for episode in range(num_episodes):
        avg_returns.append(0.)
        avg_steps.append(0)
        eps = eps_min + (eps_max - eps_min) * np.exp(-eps_dec_rate * episode)
        env.reset()
        st = env.get_agent_position()
        st = st[0] * env_size + st[1]
        for step in range(max_steps):
            act = get_action_eps_greedy_policy(env, q_tab, st, eps)
            new_st, rew, done = env.step(act)
            new_st = new_st[0] * env_size + new_st[1]
            act = act.value

            #Nemoguca akcija
            # if new_st == st:
            #     rew = -1000
            # if new_st == env.get_goal_position():
            #     rew = 1000

            q_tab[st][act] = q_tab[st][act] + lr * (rew + gamma * np.max(q_tab[new_st]) - q_tab[st][act])
            if done:
                avg_returns[-1] += rew
                avg_steps[-1] += step + 1
                break
            st = new_st
    return q_tab, avg_returns, avg_steps

def evaluate(num_episodes, max_steps, env, q_tab):
    ep_rew_lst = []
    steps_lst = []
    for episode in range(num_episodes):
        env.reset()
        st = env.get_agent_position()
        st = st[0] * env_size + st[1]
        step_cnt = 0
        ep_rew = 0
        for step in range(max_steps):
            act = np.argmax(q_tab[st])
            new_st, rew, done = env.step(environment.Action(act))
            new_st = new_st[0] * env_size + new_st[1]
            step_cnt += 1
            ep_rew += rew
            if done:
                break
            st = new_st
        ep_rew_lst.append(ep_rew)
        steps_lst.append(step_cnt)
    print(f'TEST Mean reward: {np.mean(ep_rew_lst):.2f}')
    print(f'TEST STD reward: {np.std(ep_rew_lst):.2f}')
    print(f'TEST Mean steps: {np.mean(steps_lst):.2f}')
# TEST Mean reward: 1.00
# TEST STD reward: 0.00
# TEST Mean steps: 6.00


def line_plot(data, name, show, number_of_episodes):
    pyplot.figure(f'Average {name} per episode: {np.mean(data):.2f}')

    # Deljenje liste `data` na segmente od po 100 epizoda
    segment_size = 100
    number_of_segments = len(data) // segment_size

    avg_data = [np.mean(data[i * segment_size:(i + 1) * segment_size]) for i in range(number_of_segments)]
    episodes = [segment_size * i for i in range(number_of_segments)]

    df = pandas.DataFrame({
        name: avg_data,
        'episode': episodes
    })

    plot = seaborn.lineplot(data=df, x='episode', y=name, marker='o', markersize=5, markerfacecolor='red')
    plot.get_figure().savefig(f'{name}_grafik.png')
    if show:
        pyplot.show()


number_of_episodes = 4000
max_steps = 200
learning_rate = 0.05
gamma = 0.995
epsilon_max = 1.0
epsilon_min = 0.005
epsilon_decay_rate = 0.001


try:
    q_tab, avg_returns, avg_steps = train(number_of_episodes, max_steps, learning_rate, gamma, epsilon_min, epsilon_max,
                                          epsilon_decay_rate, env)

    #print(q_tab)
    env.reset()
    st = env.get_agent_position()
    env.render(config.FPS)
    st = st[0] * env_size + st[1]
    rew_sum = 0
    while(True):
        act = np.argmax(q_tab[st])
        new_st, rew, done = env.step(environment.Action(act))
        rew_sum = rew_sum + rew
        env.render(config.FPS)
        if done:
            break
        st = new_st[0] * env_size + new_st[1]
    print("Ukupna nagrada na kraju puta iznosi ", end="")
    print(rew_sum)

    line_plot(avg_returns, "returns", True, number_of_episodes)
    line_plot(avg_steps, "steps", True, number_of_episodes)

    #evaluate(100, max_steps, env, q_tab)

    # while True:
    #     action = env.get_random_action()
    #     _, _, done = env.step(action)
    #     env.render(config.FPS)
    #     if done:
    #         break
except Quit:
    pass
