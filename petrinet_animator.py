import os
import re
import numpy as np
import tkinter as tk

# Constants for graphics
PLACE_RADIUS    = 20
TRANSITION_SIZE = 10
TOKEN_OFFSET_Y  = 30
DELAY           = 1

def initialize_data():
    # Define incidence matrix and initial marking
    incidence_matrix = np.array([
        # T0, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16
        [1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P0
        [0, -1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P1
        [0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P2
        [0, -1, -1,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1,  1], # P3
        [0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P4
        [0,  0, -1,  0,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P5
        [0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P6
        [0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0,  0], # P7
        [0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0], # P8
        [0,  0,  0,  0,  0, -1, -1,  0,  0,  1,  1,  0,  0,  0,  0,  0,  0], # P9
        [0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0], # P10
        [0,  0,  0,  0,  0,  0, -1,  0,  0,  0,  1,  0,  0,  0,  0,  0,  0], # P11
        [0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0,  0], # P12
        [0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0,  0,  0,  0], # P13
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, -1, -1,  0,  0,  0,  0], # P14
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1, -1,  1,  1,  0,  0], # P15
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0,  0], # P16
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  0, -1,  0,  0], # P17
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, -1,  0], # P18
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1, -1], # P19
        [0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1,  1]  # P20
    ])

    initial_marking = [0, 1, 0, 3, 0, 1, 0, 1, 0, 2, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1]

    # Define positions for places and transitions
    place_positions = [
        (250, 50),                                                  # P0                                        
        (50, 150), (150, 250), (250, 250), (350, 250), (450, 150),  # P1, P2, P3, P4, P5
        (250, 400),                                                 # P6
        (50, 450), (150, 525), (250, 600), (350, 525), (450, 450),  # P7, P8, P9, P10, P11
        (150, 675), (350, 675),                                     # P12, P13
        (700, 675),                                                 # P14
        (700, 525), (600, 525), (800, 525),                         # P16, P15, P17
        (700, 325),                                                 # P18       
        (700, 150), (800, 150)                                      # P19, P20
    ]

    transition_positions = [
        (150, 50),              # T0
        (150, 150), (350, 150), # T1, T2
        (150, 325), (350, 325), # T3, T4
        (150, 450), (350, 450), # T5, T6
        (150, 600), (350, 600), # T7, T8
        (150, 750), (350, 750), # T9, T10
        (600, 600), (800, 600), # T11, T12
        (600, 450), (800, 450), # T13, T14
        (700, 250),             # T15
        (700, 50)               # T16
    ]

    # Define connections between places and transitions
    place_to_transitions = {
        0: [1, 2],
        1: [1],
        2: [3],
        3: [1, 2, 15],
        4: [4],
        5: [2],
        6: [5, 6],
        7: [5],
        8: [7],
        9: [5, 6],
        10: [8],
        11: [6],
        12: [9],
        13: [10],
        14: [11, 12],
        15: [11, 12],
        16: [13],
        17: [14],
        18: [15],
        19: [16],
        20: [15]    
    }

    transition_to_places = {
        0: [0],
        1: [2],
        2: [4],
        3: [1, 3, 6],
        4: [3, 5, 6],
        5: [8],
        6: [10],
        7: [12],
        8: [13],
        9: [9, 7, 14],
        10: [9, 11, 14],
        11: [16],
        12: [17],
        13: [15, 18],
        14: [15, 18],
        15: [19],
        16: [3, 20]
    }

    return incidence_matrix, initial_marking, place_positions, transition_positions, place_to_transitions, transition_to_places

def setup_tkinter():
    """Set up the main Tkinter window and canvas for drawing the Petri net."""

    root = tk.Tk()
    root.title("Petri Net Animator")
    
    frame = tk.Frame(root)
    frame.pack(fill=tk.BOTH, expand=1)

    canvas = tk.Canvas(root, width=900, height=800)
    canvas.pack()

    total_tokens_label = tk.Label(root, text="Total Tokens: 0", font=('Arial', 12))
    total_tokens_label.pack()
    
    return root, canvas, total_tokens_label

def draw_places(canvas, place_positions):
    """Draw places as circles on the canvas and label them with their index."""

    place_circles = [
        canvas.create_oval(x - PLACE_RADIUS, y - PLACE_RADIUS, x + PLACE_RADIUS, y + PLACE_RADIUS, 
                           fill="white", outline="blue")
        for x, y in place_positions
    ]
    
    # Label the places
    for i, (x, y) in enumerate(place_positions):
        canvas.create_text(x, y, text=f"P{i}", font=('Arial', 10))
    
    return place_circles

def draw_transitions(canvas, transition_positions, transition_counts):
    """Draw transitions as rectangles on the canvas and label them with a counter."""

    transition_rects = [
        canvas.create_rectangle(x - TRANSITION_SIZE, y - TRANSITION_SIZE, x + TRANSITION_SIZE, y + TRANSITION_SIZE, 
                                fill="black")
        for x, y in transition_positions
    ]
    
    # Label the transitions with firing count
    for i, (x, y) in enumerate(transition_positions):
        canvas.create_text(x, y - 20, text=f"T{i:02}", font=('Arial', 10))
        canvas.create_text(x, y + 20, text=f"{transition_counts[i]} time(s)", font=('Arial', 8), tags=f"count_{i}")
    
    return transition_rects

def update_transition_count(canvas, transition_positions, transition_counts, transition_index):
    """Update the firing count displayed for a transition."""

    transition_counts[transition_index] += 1
    x, y = transition_positions[transition_index]
    # Remove old firing count display
    canvas.delete(f"count_{transition_index}")
    # Add updated firing count
    canvas.create_text(x, y + 20, text=f"{transition_counts[transition_index]} time(s)", font=('Arial', 8), tags=f"count_{transition_index}")

def connect_places_to_transitions(canvas, place_positions, transition_positions, place_to_transitions, transition_to_places):
    """Draw arrows from places to transitions and vice versa on the canvas."""

    for place_idx, transitions in place_to_transitions.items():
        place_pos = place_positions[place_idx]
        for trans_idx in transitions:
            trans_pos = transition_positions[trans_idx]
            canvas.create_line(place_pos[0], place_pos[1], trans_pos[0], trans_pos[1], arrow=tk.LAST, fill="gray")

    for trans_idx, places in transition_to_places.items():
        trans_pos = transition_positions[trans_idx]
        for place_idx in places:
            place_pos = place_positions[place_idx]
            canvas.create_line(trans_pos[0], trans_pos[1], place_pos[0], place_pos[1], arrow=tk.LAST, fill="gray")

def update_tokens(canvas, place_positions, place_circles, current_marking, total_tokens_label):
    """Update token counts on places and adjust the canvas accordingly."""

    total_tokens = sum(current_marking)
    total_tokens_label.config(text=f"Total Tokens: {total_tokens}")
    
    for i, token_count in enumerate(current_marking):
        x, y = place_positions[i]
        # Update place color based on token count
        canvas.itemconfig(place_circles[i], fill="red" if token_count > 0 else "white")
        # Remove old token count display
        canvas.delete(f"tokens_{i}")
        # Add new token count if any
        if token_count > 0:
            canvas.create_text(x, y + TOKEN_OFFSET_Y, text=f"{token_count} token(s)", font=('Arial', 8), tags=f"tokens_{i}", fill="black")

def fire_transition(incidence_matrix, current_marking, transition_index):
    """Attempt to fire a transition and update the marking if successful."""

    if all(current_marking[i] + incidence_matrix[i][transition_index] >= 0 for i in range(len(current_marking))):
        for i in range(len(current_marking)):
            current_marking[i] += incidence_matrix[i][transition_index]
        print(f"Fired transition: T{transition_index}")
        return True
    return False

def next_transition(sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, transition_positions, transition_counts):
    """Fire the next transition in the sequence."""

    if not sequence:
        print("No more transitions in sequence.")
        return
    
    transition_index = int(sequence.pop(0))  # Get next transition
    if fire_transition(incidence_matrix, current_marking, transition_index):
        update_tokens(canvas, place_positions, place_circles, current_marking, total_tokens_label)
        update_transition_count(canvas, transition_positions, transition_counts, transition_index)

def auto_play(sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, initial_marking, transition_positions, transition_counts, delay=1000):
    """Automatically play through the sequence of transitions with a delay."""

    if sequence:
        next_transition(sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, transition_positions, transition_counts)
        canvas.after(delay, auto_play, sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, initial_marking, transition_positions, transition_counts, delay)
    else:
        print(f"Initial marking: {initial_marking}")
        print(f"Final marking:   {current_marking}")

def load_transition_sequence(file_path):
    """Load the sequence of transitions from a file."""
    
    try:
        with open(file_path) as f:
            sequence = f.read().split("T")[1:]
        return sequence
    except FileNotFoundError:
        print(f"Error: {file_path} not found.")
        return []

def main():

    incidence_matrix, initial_marking, place_positions, transition_positions, place_to_transitions, transition_to_places = initialize_data()
    
    root, canvas, total_tokens_label = setup_tkinter()
    
    # Initialize transition firing counters
    transition_counts = [0] * len(transition_positions)
    
    place_circles = draw_places(canvas, place_positions)
    draw_transitions(canvas, transition_positions, transition_counts)
    connect_places_to_transitions(canvas, place_positions, transition_positions, place_to_transitions, transition_to_places)
    
    sequence = load_transition_sequence('Secuencia/Secuencia.txt')
    
    current_marking = list(initial_marking)
    update_tokens(canvas, place_positions, place_circles, current_marking, total_tokens_label)
    
    # Buttons for controlling the transitions
    next_button = tk.Button(root, text="Next Transition", command=lambda: next_transition(sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, transition_positions, transition_counts))
    next_button.pack()
    
    play_button = tk.Button(root, text="Play", command=lambda: auto_play(sequence, incidence_matrix, current_marking, canvas, place_positions, place_circles, total_tokens_label, initial_marking, transition_positions, transition_counts, DELAY))
    play_button.pack()
    
    root.mainloop()

if __name__ == "__main__":
    main()
