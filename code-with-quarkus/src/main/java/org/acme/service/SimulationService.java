package org.acme.service;

import org.acme.model.CorpsCeleste;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class SimulationService {
    private static final double G = 6.67430e-111  ;
    private static final double DT = 0.000001;
    private static final double MIN_DISTANCE = 0.0001;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Random random = new Random();

    public void startSimulation(List<CorpsCeleste> corps) {
        if (running.get()) return;
        running.set(true);

        executor.scheduleAtFixedRate(() -> {
            if (running.get()) {
                updatePositions(corps);
            }
        }, 0, 5, TimeUnit.MILLISECONDS);
    }

    public void stopSimulation(List<CorpsCeleste> corps) {
        running.set(false);
        corps.clear();
    }

    public List<CorpsCeleste> generateRandomBodies(int n) {
        List<CorpsCeleste> bodies = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            bodies.add(randomBody());
        }
        return bodies;
    }

    private CorpsCeleste randomBody() {
        double angle = random.nextDouble() * 2 * Math.PI;
        double radius = random.nextDouble();
        double x = radius * Math.cos(angle);
        double y = radius * Math.sin(angle);
        double vx = radius * Math.cos(angle);
        double vy = radius * Math.sin(angle);
        return new CorpsCeleste(x, y, vx, vy, 1.0);
    }

    private void updatePositions(List<CorpsCeleste> corps) {
        int n = corps.size();
        double[] ax = new double[n];
        double[] ay = new double[n];

        for (int i = 0; i < n; i++) {
            CorpsCeleste c1 = corps.get(i);
            for (int j = i + 1; j < n; j++) {
                CorpsCeleste c2 = corps.get(j);
                double dx = c2.x - c1.x;
                double dy = c2.y - c1.y;
                double distanceSquared = dx * dx + dy * dy;
                double distance = Math.sqrt(distanceSquared);
                double safeDistance = Math.max(distanceSquared, MIN_DISTANCE);
                double force = (G * c1.masse * c2.masse) / (safeDistance * distance);
                
                double fx = force * (dx / distance);
                double fy = force * (dy / distance);
                
                ax[i] += fx / c1.masse;
                ay[i] += fy / c1.masse;
                
                ax[j] -= fx / c2.masse;
                ay[j] -= fy / c2.masse;
            }
        }

        for (int i = 0; i < n; i++) {
            CorpsCeleste c = corps.get(i);
            c.vx += ax[i] * DT;
            c.vy += ay[i] * DT;
            c.x += c.vx * DT;
            c.y += c.vy * DT;
        }
    }
}
