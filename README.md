ssh jne2a6000@192.168.0.52

docker run --name isaac-sim-android-test --entrypoint bash -it --runtime=nvidia --gpus all   -e "ACCEPT_EULA=Y"   --rm --network=host   -e "PRIVACY_CONSENT=Y"   -v ~/docker/isaac-sim/cache/kit:/isaac-sim/kit/cache:rw   -v ~/docker/isaac-sim/cache/ov:/root/.cache/ov:rw   -v ~/docker/isaac-sim/cache/pip:/root/.cache/pip:rw   -v ~/docker/isaac-sim/cache/glcache:/root/.cache/nvidia/GLCache:rw   -v ~/docker/isaac-sim/cache/computecache:/root/.nv/ComputeCache:rw   -v ~/docker/isaac-sim/logs:/root/.nvidia-omniverse/logs:rw   -v ~/docker/isaac-sim/data:/root/.local/share/ov/data:rw   -v ~/docker/isaac-sim/documents:/root/Documents:rw   -v ~/zivid-isaac-sim:/isaac-sim/zivid-isaac-sim:rw   nvcr.io/nvidia/isaac-sim:5.0.0

./isaac-sim.streaming.sh   --allow-root   --ext-folder /isaac-sim/zivid-isaac-sim/source   --enable isaacsim.zivid
