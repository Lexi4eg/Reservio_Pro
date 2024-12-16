using System;
using System.Collections.Generic;
using System.Linq;
using Avalonia.Controls;
using Avalonia.Interactivity;

namespace Reservio
{
    public partial class Bereichsauswahl : UserControl
    {
        private string Personenanzahl { get; }
        private DateTime Datum { get; }
        
        private readonly Dictionary<string, int> tischKapazitäten = new Dictionary<string, int>
        {
            { "Lounge", 2 },
            { "Saal", 8 },
            { "Freibereich", 3 },
            { "Gang", 8 },
            { "Terrasse", 6 }
        };
        
        public Bereichsauswahl(string personenanzahl, DateTime datum)
        {
            InitializeComponent();
            Personenanzahl = personenanzahl;
            Datum = datum;
            int max_personenanzahl = int.Parse(personenanzahl);
            
            var erlaubteBereiche = tischKapazitäten
                .Where(kv => kv.Value >= max_personenanzahl)
                .Select(kv => kv.Key);

            foreach (var tisch in erlaubteBereiche)
            {
                areaComboBox.Items.Add(new ComboBoxItem { Content = tisch, Name = tisch});
            }
        }
        
        private void OnWeiterButtonClick(object sender, RoutedEventArgs e)
        {
            var selectedItem = areaComboBox.SelectedItem as ComboBoxItem;

            if (selectedItem == null)
            {
                Fehlermeldung.Text = "Bitte wählen Sie einen Bereich aus.";
                return;
            }

            string selectedArea = selectedItem.Content.ToString();

            switch (selectedArea)
            {
                case "Lounge":
                    this.Content = new LoungePage(Personenanzahl, Datum);
                    break;
                case "Gang":
                    this.Content = new GangPage(Personenanzahl, Datum);
                    break;
                case "Saal":
                    this.Content = new SaalPage(Personenanzahl, Datum);
                    break;
                case "Terrasse":
                    this.Content = new TerrassenPage(Personenanzahl, Datum);
                    break;
                case "Freibereich":
                    this.Content = new FreibereichPage(Personenanzahl, Datum);
                    break;
                default:
                    Fehlermeldung.Text = "Ungültiger Bereich ausgewählt.";
                    break;
            }
        }
        private void OnZurückButtonClick(object sender, RoutedEventArgs e)
        {
            this.Content = new Datenerhebung();
        }
    }
}
