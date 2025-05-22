import React from 'react';

const TodoItem = ({ todo, onDelete }) => (
  <li className="todo-item">
    <span>{todo.title}</span>
    <button onClick={onDelete}>Delete</button>
  </li>
);

export default TodoItem;
