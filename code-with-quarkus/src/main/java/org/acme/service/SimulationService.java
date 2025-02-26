package org.acme.service;

import org.acme.model.CorpsCeleste;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SimulationService {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public void startSimulation(List<CorpsCeleste> corps, double dt) {
        if (running.get()) return; // Empêcher plusieurs exécutions

        running.set(true);
        executor.scheduleAtFixedRate(() -> {
            if (running.get()) {
                updatePositions(corps, dt);
            }
        }, 0, 5, TimeUnit.MILLISECONDS); // Mise à jour toutes les 5ms
    }

    public void stopSimulation(List<CorpsCeleste> corps) {
        running.set(false);
        corps.clear(); // Supprimer les corps célestes
    }

    private static final double G = 6.67430e-11 * 1e6; // Échelle de la gravité pour l'affichage
    private static final double COLLISION_RADIUS = 2.0; // Distance pour la fusion

    private void updatePositions(List<CorpsCeleste> corps, double dt) {
        int n = corps.size();
        double[] ax = new double[n];
        double[] ay = new double[n];
        List<CorpsCeleste> toRemove = new ArrayList<>();

        // Calcul des forces gravitationnelles
        for (int i = 0; i < n; i++) {
            CorpsCeleste c1 = corps.get(i);
            for (int j = i + 1; j < n; j++) { // Parcours optimisé pour éviter les doublons
                CorpsCeleste c2 = corps.get(j);
                double dx = c2.x - c1.x;
                double dy = c2.y - c1.y;
                double distance = Math.sqrt(dx * dx + dy * dy) + 1e-10;

                if (distance < COLLISION_RADIUS) {
                    // Fusion des corps
                    if (c1.masse >= c2.masse) {
                        absorberCorps(c1, c2);
                        toRemove.add(c2);
                    } else {
                        absorberCorps(c2, c1);
                        toRemove.add(c1);
                        break;
                    }
                } else {
                    // Force gravitationnelle F = G * (m1 * m2) / r^2
                    double force = (G * c1.masse * c2.masse) / (distance * distance);

                    // Accélération a = F / m
                    ax[i] += force * (dx / distance) / c1.masse;
                    ay[i] += force * (dy / distance) / c1.masse;

                    ax[j] -= force * (dx / distance) / c2.masse;
                    ay[j] -= force * (dy / distance) / c2.masse;
                }
            }
        }

        // Mise à jour des vitesses et positions
        for (int i = 0; i < n; i++) {
            if (toRemove.contains(corps.get(i))) continue; // Ne pas traiter les corps à supprimer
            CorpsCeleste c = corps.get(i);
            c.vx += ax[i] * dt;
            c.vy += ay[i] * dt;
            c.x += c.vx * dt;
            c.y += c.vy * dt;

            // Debugging : Vérifier les forces appliquées
            System.out.println("Corps " + i + ": ax=" + ax[i] + ", ay=" + ay[i] + " | vx=" + c.vx + ", vy=" + c.vy);
        }

        // Supprimer les corps fusionnés
        corps.removeAll(toRemove);
    }

    // Fonction de fusion des corps célestes
    private void absorberCorps(CorpsCeleste plusGros, CorpsCeleste plusPetit) {
        plusGros.masse += plusPetit.masse; // Masse combinée
        // Conservation de la quantité de mouvement
        plusGros.vx = (plusGros.vx * plusGros.masse + plusPetit.vx * plusPetit.masse) / plusGros.masse;
        plusGros.vy = (plusGros.vy * plusGros.masse + plusPetit.vy * plusPetit.masse) / plusGros.masse;
    }
}
