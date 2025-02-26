import { useEffect, useRef } from "react";

export default function SimulationCanvas({ corpsCelestes, width, height }) {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext("2d");

    let animationFrameId;
    
    const render = () => {
      ctx.clearRect(0, 0, width, height);
      
      if (corpsCelestes.length === 0) return;

      // Trouver les bornes pour centrer l'affichage
      const xMin = Math.min(...corpsCelestes.map(c => c.x));
      const xMax = Math.max(...corpsCelestes.map(c => c.x));
      const yMin = Math.min(...corpsCelestes.map(c => c.y));
      const yMax = Math.max(...corpsCelestes.map(c => c.y));

      // Centre des positions des corps célestes
      const xCenter = (xMin + xMax) / 2;
      const yCenter = (yMin + yMax) / 2;

      // Échelle basée sur la taille du canvas
      const scale = Math.min(width / (xMax - xMin + 1), height / (yMax - yMin + 1)) * 0.9; // 0.9 pour un peu de marge

      ctx.save();
      ctx.translate(width / 2, height / 2); // Déplacer l'origine au centre du canvas

      // Dessiner les corps célestes
      corpsCelestes.forEach((corps) => {
        ctx.beginPath();
        ctx.arc(
          (corps.x - xCenter) * scale, // Ajuster par rapport au centre
          (corps.y - yCenter) * scale,
          Math.sqrt(corps.masse) * 2, // Taille proportionnelle à la masse
          0,
          2 * Math.PI
        );
        ctx.fillStyle = "white";
        ctx.fill();
        ctx.strokeStyle = "gray";
        ctx.stroke();
      });

      ctx.restore();
      animationFrameId = requestAnimationFrame(render);
    };

    render();
    return () => cancelAnimationFrame(animationFrameId);
  }, [corpsCelestes, width, height]);

  return (
    <canvas
      ref={canvasRef}
      width={width}
      height={height}
      style={{
        background: "black",
        display: "block",
        borderRadius: "10px",
      }}
    />
  );
}
