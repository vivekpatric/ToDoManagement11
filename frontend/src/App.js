import React, { useEffect, useState } from 'react';
import axios from 'axios';
import TodoItem from './components/TodoItem';
import './App.css';

const API = process.env.REACT_APP_API_URL;

function App() {
  const [todos, setTodos] = useState([]);
  const [title, setTitle] = useState('');
  const [message, setMessage] = useState('');

  const fetchTodos = async () => {
    const res = await axios.get(`${API}/todos`);
    setTodos(res.data);
  };

  const addTodo = async () => {
    if (!title.trim()) return;
    await axios.post(`${API}/todos`, { title });
    setTitle('');
    fetchTodos();
  };

  const deleteTodo = async (id) => {
    await axios.delete(`${API}/todos/${id}`);
    fetchTodos();
  };

  const summarize = async () => {
    try {
      const res = await axios.post(`${API}/summarize`);
      setMessage(res.data || 'Summary sent!');
    } catch (error) {
      setMessage('Failed to send summary.');
    }
  };

  useEffect(() => {
    fetchTodos();
  }, []);

  return (
    <div className="container">
      <h1>Todo Summary Assistant</h1>

      <div className="input-container">
        <input
          type="text"
          value={title}
          placeholder="Enter todo"
          onChange={(e) => setTitle(e.target.value)}
        />
        <button onClick={addTodo}>Add</button>
      </div>

      <ul className="todo-list">
        {todos.map((todo) => (
          <TodoItem key={todo.id} todo={todo} onDelete={() => deleteTodo(todo.id)} />
        ))}
      </ul>

      <button className="summary-btn" onClick={summarize}>
        Generate & Send Summary
      </button>

      {message && <div className="message">{message}</div>}
    </div>
  );
}

export default App;

