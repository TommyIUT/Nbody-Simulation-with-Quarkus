import { useState } from "react";
import axios from "axios";

const API_URL = "http://localhost:8080/simulate";

export default function SimulationControls() {
  const [loading, setLoading] = useState(false);
  const [loadingBody, setLoadingBody] = useState(false);

  const addRandomBody = async () => {
    setLoadingBody(true);
    const minMasse = 50;  // Masse minimale pour éviter les valeurs trop faibles
    const maxMasse = 500; // Masse maximale pour éviter les déséquilibres

    const body = {
      x: Math.random() * 100,
      y: Math.random() * 100,
      vx: 0,
      vy: 0,
      masse: minMasse + (maxMasse - minMasse) * Math.pow(Math.random(), 2),
    };


    try {
      await axios.post(`${API_URL}/add`, body);
      console.log("Corps céleste ajouté !");
    } catch (error) {
      console.error("Erreur lors de l'ajout :", error);
    }
    setLoadingBody(false);
  };

  const stopSimulation = async () => {
    try {
      await axios.post(`${API_URL}/stop`);
      console.log("Simulation arrêtée !");
    } catch (error) {
      console.error("Erreur lors de l'arrêt :", error);
    }
  };

  const startSimulation = async () => {
    setLoading(true);
    try {
      await axios.post(`${API_URL}/start`);
      console.log("Simulation démarrée !");
    } catch (error) {
      console.error("Erreur lors du démarrage :", error);
    }
    setLoading(false);
  };

  return (
    <div className="flex flex-col gap-4 p-4">
      <button
        className="bg-blue-500 text-white px-4 py-2 rounded disabled:opacity-50"
        onClick={addRandomBody}
        disabled={loadingBody}
      >
        {loadingBody ? "Ajout en cours..." : "Ajouter un corps céleste"}
      </button>
      <button
        className="bg-green-500 text-white px-4 py-2 rounded disabled:opacity-50"
        onClick={startSimulation}
        disabled={loading}
      >
        {loading ? "Démarrage..." : "Lancer la simulation"}
      </button>
      <button className="bg-red-500 text-white px-4 py-2 rounded" onClick={stopSimulation}>
        Arrêter la simulation
      </button>
    </div>
  );
}
