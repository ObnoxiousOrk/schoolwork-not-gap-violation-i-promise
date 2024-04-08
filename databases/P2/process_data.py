import hashlib

import numpy as np
import pandas as pd

data = pd.read_excel(
    "all_data.ods",
    ["Species", "Families", "Common names", "Users", "Sightings"],
    engine="odf",
)

species = data["Species"]
families = data["Families"]
common_names = data["Common names"]
users = data["Users"]
sightings = data["Sightings"]

species["genus_name"] = species["Species name"].map(lambda name: name.split(" ")[0])
species["species_epithet"] = species["Species name"].map(
    lambda name: name.split(" ")[1]
)
species.drop(columns=["Species name"], inplace=True)
# print(species.head())

common_names["genus_name"] = common_names["Scientific name"].map(
    lambda name: name.split(" ")[0]
)
common_names["species_epithet"] = common_names["Scientific name"].map(
    lambda name: name.split(" ")[1]
)
common_names.drop(columns=["Scientific name"], inplace=True)

users.drop(columns=["Unnamed: 4"], inplace=True)
users["Unnamed: 5"] = users["Unnamed: 5"][1:-2].map(
    lambda password: hashlib.sha256((password + "BIRDS").encode("utf-8")).hexdigest()
    # lambda x: print(type(x))
)
users = users[1:-2]
# print(users.head())

# print(users.tail())

sightings["genus_name"] = sightings["Scientific name"].map(
    lambda name: name.split(" ")[0]
)
sightings["species_epithet"] = sightings["Scientific name"].map(
    lambda name: name.split(" ")[1]
)
sightings.drop(columns=["Scientific name"], inplace=True)
sightings["id"] = np.nan
cols = sightings.columns.tolist()
cols = cols[-1:] + cols[:-1]
sightings = sightings[cols]

species.to_csv("species.csv", index=False, header=False, na_rep="NULL")
families.to_csv("families.csv", index=False, header=False, na_rep="NULL")
common_names.to_csv("common_names.csv", index=False, header=False, na_rep="NULL")
users.to_csv("users.csv", index=False, header=False, na_rep="NULL")
sightings.to_csv("sightings.csv", index=False, header=False, na_rep="NULL")
