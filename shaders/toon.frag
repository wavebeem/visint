varying float NdotL;

void main() 
{
  vec3 SurfaceColor = vec3(gl_FrontMaterial.diffuse);
  float k = 8.0;

  // Produces the stair step pattern
  float scale = ceil(k * NdotL) / k;

  gl_FragColor = vec4(SurfaceColor * scale, 1.0);
}
