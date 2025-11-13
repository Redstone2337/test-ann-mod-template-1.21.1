BrewingRecipeEvent.create(e => {
    let {input, material, output} = e;
    input.add(Items.POTION)
        .component(DataComponentTypes.POTION_CONTENTS, Potions.WATER)
        .toCreateBrewing();
    material.add(Items.GLOWSTONE_DUST).toCreateBrewing();
    output.add(Items.POTION)
        .component(DataComponentTypes.POTION_CONTENTS, Potions.NIGHT_VISION)
        .toCreateBrewing();
})

brew({
  input: {item: 'potion', potion: 'awkward'},
  material: 'obsidian',
  output: {item: 'potion', potion: 'health_boost'}
})