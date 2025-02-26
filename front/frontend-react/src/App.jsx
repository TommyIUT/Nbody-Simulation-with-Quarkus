import { useState, useEffect } from "react";
import axios from "axios";
import SimulationControls from "./SimulationControls";
import SimulationCanvas from "./SimulationCanvas";

function App() {
  const [corpsCelestes, setCorpsCelestes] = useState([]);
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const [windowHeight, setWindowHeight] = useState(window.innerHeight * 0.8); // 80% de la hauteur

  useEffect(() => {
    const fetchCorpsCelestes = async () => {
      try {
        const response = await axios.get("http://localhost:8080/simulate/state");
        setCorpsCelestes(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des corps célestes", error);
      }
    };
  
    fetchCorpsCelestes();
    const interval = setInterval(fetchCorpsCelestes, 33); // Mise à jour toutes les 33ms
  
    return () => clearInterval(interval);
  }, []);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
      setWindowHeight(window.innerHeight * 0.8);
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <div className="container">
      <h1>Simulation N-Body</h1>
      <SimulationControls />
      <SimulationCanvas corpsCelestes={corpsCelestes} width={1000} height={windowHeight} />
    </div>
  );
}

export default App;
