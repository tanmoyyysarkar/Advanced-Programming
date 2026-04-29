import React, { useState } from "react";

export default function TodoApp() {
  const [todos, setTodos] = useState([]);
  const [input, setInput] = useState("");

  // Add todo using form submit
  const handleSubmit = (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    setTodos([...todos, { task: input, done: false }]);
    setInput("");
  };

  // Toggle done
  const toggleDone = (index) => {
    const updatedTodos = todos.map((todo, i) =>
      i === index ? { ...todo, done: !todo.done } : todo,
    );
    setTodos(updatedTodos);
  };

  // Delete todo
  const deleteTodo = (index) => {
    setTodos(todos.filter((_, i) => i !== index));
  };

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-xl mx-auto bg-white shadow-lg rounded-xl p-6 space-y-6">
        <h1 className="text-2xl font-bold text-slate-800">Todo List</h1>

        {/* Form */}
        <form onSubmit={handleSubmit} className="flex gap-2">
          <input
            className="border p-2 rounded flex-1"
            type="text"
            placeholder="Enter a todo..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button className="bg-indigo-600 text-white px-4 rounded hover:bg-indigo-700">
            Add
          </button>
        </form>

        {/* Todo List */}
        <ul className="space-y-2">
          {todos.map((todo, index) => (
            <li
              key={index}
              className="flex justify-between items-center bg-slate-100 p-3 rounded"
            >
              <span
                className={`flex-1 ${
                  todo.done ? "line-through text-slate-400" : ""
                }`}
              >
                {todo.task}
              </span>

              <div className="flex gap-2">
                <button
                  onClick={() => toggleDone(index)}
                  className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                >
                  {todo.done ? "Undo" : "Done"}
                </button>

                <button
                  onClick={() => deleteTodo(index)}
                  className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
}
