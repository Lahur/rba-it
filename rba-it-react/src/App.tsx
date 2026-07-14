import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import Sidebar from './components/sidebar.tsx'
import Toast from './components/Toast.tsx'
import { routes } from './routes.tsx'

function App() {
  return (
      <BrowserRouter>
        <Sidebar/>
        <Toast/>
        <div className="sm:ml-64 p-6">
          <Routes>
            {routes.map((route) => (
                <Route key={route.path} path={route.path} element={route.element}/>
            ))}
            <Route path="/" element={<Navigate to={routes[0].path} replace/>}/>
          </Routes>
        </div>
      </BrowserRouter>
  );
}

export default App;