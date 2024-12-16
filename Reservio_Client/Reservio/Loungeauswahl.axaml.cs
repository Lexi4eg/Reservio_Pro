using System;
using System.Collections.Generic;
using System.Linq;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class LoungePage : UserControl
    {
       private string Personenanzahl { get; }
        private DateTime Datum { get; }

        // Kapazitäten der Tische
        private readonly Dictionary<string, int> tischKapazitäten = new Dictionary<string, int>
        {
            { "L1", 2 },
            { "L2", 2 },
            { "L3", 2 },
            { "L4", 2 },
            { "L5", 2 },
            { "L6", 2 }
        };

        public LoungePage(string personenanzahl, DateTime datum)
        {
            InitializeComponent();
            int personenanzahl_ = int.Parse(personenanzahl);
            Datum = datum;
            Personenanzahl = personenanzahl;
            
            var erlaubteTische = tischKapazitäten
                .Where(kv => kv.Value >= personenanzahl_)
                .Select(kv => kv.Key);

            foreach (var tisch in erlaubteTische)
            {
                areaComboBox.Items.Add(new ComboBoxItem { Content = tisch });
            }
        }

        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            if (areaComboBox.SelectedItem != null)
            {
                string selectedTable = (areaComboBox.SelectedItem as ComboBoxItem)?.Content?.ToString();

                this.Content = new Kontaktdaten(selectedTable, Personenanzahl, Datum);
            }
            else
            {
                Fehlermeldung.Text = "Bitte wählen Sie einen Tisch aus.";
            }
        }

        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new Bereichsauswahl(Personenanzahl, Datum);
        }
    }
}