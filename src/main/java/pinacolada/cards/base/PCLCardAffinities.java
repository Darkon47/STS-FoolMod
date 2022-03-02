package pinacolada.cards.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import eatyourbeets.cards.base.EYBCardAffinities;
import eatyourbeets.cards.base.EYBCardAffinity;
import eatyourbeets.utilities.AdvancedTexture;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.ArrayList;

import static pinacolada.cards.base.PCLAffinity.General;
import static pinacolada.cards.base.PCLAffinity.TOTAL_AFFINITIES;

public class PCLCardAffinities
{
    private static final AdvancedTexture upgradeCircle = new AdvancedTexture(PGR.PCL.Images.Circle.Texture(), Settings.GREEN_RELIC_COLOR);

    protected PCLCardAffinity[] List = new PCLCardAffinity[TOTAL_AFFINITIES];
    protected final ArrayList<PCLCardAffinity> Sorted = new ArrayList<>();
    public PCLCard Card;
    public PCLCardAffinity Star = null;
    public boolean displayUpgrades = false;
    public boolean collapseDuplicates = false;

    public PCLCardAffinities(PCLCard card)
    {
        Card = card;
        this.UpdateSortedList();
    }

    public PCLCardAffinities(PCLCard card, EYBCardAffinities affinities)
    {
        Card = card;
        Initialize(affinities);

    }

    public PCLCardAffinities(PCLCard card, PCLCardAffinities affinities)
    {
        Card = card;
        Initialize(affinities);
    }

    public PCLCardAffinities Initialize(PCLAffinity affinity, int base, int upgrade, int scaling, int requirement)
    {
        if (base > 0 || upgrade > 0 || scaling > 0 || requirement > 0)
        {
            PCLCardAffinity a = Set(affinity, base);
            a.upgrade = upgrade;
            a.scaling = scaling;
            a.requirement = requirement;
        }

        this.UpdateSortedList();
        return this;
    }

    public PCLCardAffinities Initialize(EYBCardAffinities affinities)
    {
        if (affinities.Star != null)
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, affinities.Star.level);
            Star.scaling = affinities.Star.scaling;
            Star.upgrade = affinities.Star.upgrade;
            Star.requirement = affinities.Star.requirement;
        }
        else
        {
            Star = null;
        }

        List = new PCLCardAffinity[TOTAL_AFFINITIES];
        for (EYBCardAffinity a : affinities.List)
        {
            PCLCardAffinity t = new PCLCardAffinity(PCLGameUtilities.ConvertEYBToPCLAffinity(a.type), a.level);
            if (t.type.ID < 0) {
                continue;
            }
            t.scaling = a.scaling;
            t.upgrade = a.upgrade;
            t.requirement = a.requirement;
            List[t.type.ID] = t;
        }

        this.UpdateSortedList();
        return this;
    }

    public PCLCardAffinities Initialize(PCLCardAffinities affinities)
    {
        if (affinities.Star != null)
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, affinities.Star.level);
            Star.scaling = affinities.Star.scaling;
            Star.upgrade = affinities.Star.upgrade;
            Star.requirement = affinities.Star.requirement;
        }
        else
        {
            Star = null;
        }

        List = new PCLCardAffinity[TOTAL_AFFINITIES];
        for (PCLCardAffinity a : affinities.GetCardAffinities(false))
        {
            PCLCardAffinity t = new PCLCardAffinity(a.type, a.level);
            t.scaling = a.scaling;
            t.upgrade = a.upgrade;
            t.requirement = a.requirement;
            List[t.type.ID] = t;
        }

        this.UpdateSortedList();
        return this;
    }

    public void Clear()
    {
        List = new PCLCardAffinity[TOTAL_AFFINITIES];
        Star = null;
        this.UpdateSortedList();
    }

    public void UpdateSortedList() {
        Sorted.clear();
        for (PCLCardAffinity a : List)
        {
            if (a == null || a.level <= 0)
            {
                continue;
            }

            if (collapseDuplicates) {
                Sorted.add(a);
            }
            else {
                for (int i = 0; i < a.level; i++) {
                    Sorted.add(a);
                }
            }
        }

        if (Sorted.isEmpty()) {
            Sorted.add(new PCLCardAffinity(General, 1));
        }

        Sorted.sort(PCLCardAffinity::compareTo);
    }

    public void ApplyUpgrades()
    {
        if (Star != null)
        {
            Star.level += Star.upgrade;
        }

        for (PCLCardAffinity a : List)
        {
            if (a != null) {
                a.level += a.upgrade;
            }
        }

        this.UpdateSortedList();
    }

    public void Add(int red, int green, int blue, int orange, int light, int dark, int silver)
    {
        Add(PCLAffinity.Red, red);
        Add(PCLAffinity.Green, green);
        Add(PCLAffinity.Blue, blue);
        Add(PCLAffinity.Orange, orange);
        Add(PCLAffinity.Light, light);
        Add(PCLAffinity.Dark, dark);
        Add(PCLAffinity.Silver, silver);
    }

    public void Set(int red, int green, int blue, int orange, int light, int dark, int silver)
    {
        Set(PCLAffinity.Red, red);
        Set(PCLAffinity.Green, green);
        Set(PCLAffinity.Blue, blue);
        Set(PCLAffinity.Orange, orange);
        Set(PCLAffinity.Light, light);
        Set(PCLAffinity.Dark, dark);
        Set(PCLAffinity.Silver, silver);
    }

    public PCLCardAffinity AddStar(int level)
    {
        return SetStar((Star == null ? 0 : Star.level) + level);
    }

    public PCLCardAffinity SetStar(int level)
    {
        if (Star != null)
        {
            Star.level = level;
        }
        else
        {
            Star = new PCLCardAffinity(PCLAffinity.Star, level);
        }

        this.UpdateSortedList();
        return Star;
    }

    public boolean HasSameAffinities(PCLCardAffinities other)
    {
        if (other == null) {
            return false;
        }
        if (this.GetLevel(PCLAffinity.Star) != other.GetLevel(PCLAffinity.Star)) {
            return false;
        }
        for (PCLAffinity af : PCLAffinity.Extended()) {
            if (this.GetLevel(af) != other.GetLevel(af)) {
                return false;
            }
        }
        return true;
    }

    public boolean HasStar()
    {
        return Star != null && Star.level > 0;
    }

    public boolean IsEmpty() {
        return (PCLJUtils.All(List, af -> af == null || af.level <= 0)) && !HasStar();
    }

    public PCLCardAffinities Add(PCLCardAffinities other, int levelLimit)
    {
        if (other != null)
        {
            int star = Math.min(levelLimit, other.GetLevel(PCLAffinity.Star));
            if (star > 0)
            {
                AddStar(star);
                //Add(star, star, star, star, star, star);
            }
            else for (PCLCardAffinity item : other.List)
            {
                if (item != null) {
                    Add(item.type, Math.min(levelLimit, item.level));
                }
            }
        }

        this.UpdateSortedList();
        return this;
    }

    public PCLCardAffinities Add(PCLCardAffinities other)
    {
        if (other.Star != null){
            AddStar(other.Star.level);
        }
        for (PCLCardAffinity item : other.List)
        {
            if (item != null) {
                Add(item.type, item.level);
            }
        }

        this.UpdateSortedList();
        return this;
    }

    public PCLCardAffinity Add(PCLAffinity affinity, int level)
    {
        if (affinity == PCLAffinity.Star)
        {
            return AddStar(level);
        }

        PCLCardAffinity a = List[affinity.ID];
        if (a != null) {
            a.level = Math.max(0, a.level + level);
            return a;
        }

        a = new PCLCardAffinity(affinity, level);
        List[affinity.ID] = a;

        this.UpdateSortedList();
        return a;
    }

    public PCLCardAffinity Set(PCLAffinity affinity, int level)
    {
        if (level < 0) {
            level = 0;
        }
        if (affinity == PCLAffinity.Star)
        {
            SetStar(level);
            return Star;
        }

        PCLCardAffinity result = List[affinity.ID];
        if (result != null) {
            result.level = level;
            return result;
        }

        result = new PCLCardAffinity(affinity, level);
        List[affinity.ID] = result;

        this.UpdateSortedList();
        return result;
    }

    public PCLCardAffinity AddScaling(PCLAffinity affinity, int level) {
        PCLCardAffinity a = Get(affinity);
        a.scaling = Math.max(0,a.scaling + level);
        return a;
    }

    public PCLCardAffinity SetScaling(PCLAffinity affinity, int level) {
        PCLCardAffinity a = Get(affinity);
        a.scaling = Math.max(0,level);
        return a;
    }

    public PCLCardAffinity Get(PCLAffinity affinity)
    {
        return Get(affinity, false);
    }

    public PCLCardAffinity Get(PCLAffinity affinity, boolean createIfNull)
    {
        if (affinity == PCLAffinity.General)
        {
            final int star = Star != null ? Star.level : 0;
            final PCLCardAffinity a = PCLJUtils.Max(List, af -> af);
            return a != null && a.level >= star ? a : Star;
        }

        if (affinity == PCLAffinity.Star)
        {
            return (createIfNull && Star == null) ? SetStar(0) : Star;
        }

        if (affinity.ID < 0 || affinity.ID >= TOTAL_AFFINITIES)
        {
            return null;
        }

        PCLCardAffinity a = List[affinity.ID];
        return a == null && createIfNull ? Set(affinity, 0) : a;
    }

    public ArrayList<PCLAffinity> GetAffinities() {
        final ArrayList<PCLAffinity> list = new ArrayList<>();
        for (PCLCardAffinity item : List)
        {
            if (item != null && item.level > 0)
            {
                list.add(item.type);
            }
        }
        return list;
    }

    public PCLAffinity[] GetAffinitiesAsArray() {
        return GetAffinities().toArray(new PCLAffinity[] {});
    }

    public ArrayList<PCLCardAffinity> GetCardAffinities(boolean filterLevelZero) {
        final ArrayList<PCLCardAffinity> list = new ArrayList<>();
        for (PCLCardAffinity item : List)
        {
            if (item != null && (!filterLevelZero || item.level > 0))
            {
                list.add(item);
            }
        }
        return list;
    }

    public int GetScaling(PCLAffinity affinity, boolean useStarScaling)
    {
        final int star = (Star != null ? Star.scaling : 0);
        if (affinity == PCLAffinity.Star)
        {
            return star;
        }

        int scaling = 0;
        if (useStarScaling)
        {
            scaling = star;
        }

        final PCLCardAffinity a = Get(affinity);
        if (a != null)
        {
            scaling += a.scaling;
        }

        return scaling;
    }

    public int GetUpgrade(PCLAffinity type)
    {
        return GetUpgrade(type, true);
    }

    public int GetUpgrade(PCLAffinity affinity, boolean useStar)
    {
        final int star = (Star != null ? Star.upgrade : 0);
        if (affinity == PCLAffinity.Star || (useStar && star > 0))
        {
            return star;
        }
        else if (affinity == null || affinity == PCLAffinity.General) // Highest level among all affinities
        {
            final PCLCardAffinity a = PCLJUtils.FindMax(List, af -> af);
            return a == null ? star : a.upgrade;
        }
        else
        {
            final PCLCardAffinity a = Get(affinity);
            return (a != null) ? a.upgrade : 0;
        }
    }

    public int GetLevel(PCLAffinity affinity)
    {
        return GetLevel(affinity, true);
    }

    public int GetLevel(PCLAffinity affinity, boolean useStarLevel)
    {
        int star = (Star != null ? Star.level : 0);
        if (affinity == PCLAffinity.Star || (useStarLevel && star > 0))
        {
            return star;
        }
        else if (affinity == PCLAffinity.General)
        {
            final PCLCardAffinity a = PCLJUtils.FindMax(List, af -> af.level);
            return a == null ? (useStarLevel ? star : 0) : a.level; // Highest level among all affinities
        }
        else
        {
            final PCLCardAffinity a = Get(affinity);
            return (a != null) ? a.level : 0;
        }
    }

    public PCLCardAffinity GetDirectly(PCLAffinity affinity)
    {
        if (affinity == PCLAffinity.Star)
        {
            return Star;
        }
        else if (affinity.ID < 0 || affinity.ID >= TOTAL_AFFINITIES)
        {
            return null;
        }

        return List[affinity.ID];
    }

    public int GetDirectLevel(PCLAffinity affinity)
    {
        final PCLCardAffinity a = GetDirectly(affinity);
        return (a != null) ? a.level : 0;
    }

    public void SetRequirement(PCLAffinity affinity, int requirement)
    {
        Get(affinity == PCLAffinity.General ? PCLAffinity.Star : affinity, true).requirement = requirement;
    }

    public int GetRequirement(PCLAffinity affinity)
    {
        final PCLCardAffinity a = Get(affinity == PCLAffinity.General ? PCLAffinity.Star : affinity);
        return a == null ? 0 : a.requirement;
    }

    public void RenderOnCard(SpriteBatch sb, PCLCard card, boolean highlight) {
        float size;
        float step;
        float y = AbstractCard.RAW_H;

        if (highlight)
        {
            size = 64;
            y *= 0.58f;
            step = size * 0.9f;
        }
        else
        {
            size = 48;//48;
            y *= 0.49f;// -0.51f;
            step = size * 1.2f;
        }

        Render(sb, card, 0, y, size, step);

    }

    public void Render(SpriteBatch sb, PCLCard card, float x, float y, float size, float step)
    {
        if (HasStar())
        {
            Star.RenderOnCard(sb, card, 0, y, size, displayUpgrades && Star.upgrade > 0, collapseDuplicates);
            return;
        }

        int max = Sorted.size();
        final int half = max / 2;
        if (half >= 2)
        {
            step *= 0.75f;
        }

        for (int i = 0; i < max; i++)
        {
            final PCLCardAffinity item = Sorted.get(i);

            if (max % 2 == 1)
            {
                x = (step * (i - half));
            }
            else
            {
                x = (step * 0.5f) + (step * (i - half));
            }

            item.RenderOnCard(sb, card, x, y, size, displayUpgrades && item.upgrade > 0, collapseDuplicates);
        }
    }
}